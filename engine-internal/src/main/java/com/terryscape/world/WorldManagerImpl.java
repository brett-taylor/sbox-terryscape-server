package com.terryscape.world;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.EntityImpl;
import com.terryscape.entity.EntityPrefabType;
import com.terryscape.entity.packet.EntityAddedOutgoingPacket;
import com.terryscape.entity.packet.EntityRemovedOutgoingPacket;
import com.terryscape.entity.packet.EntityUpdatedOutgoingPacket;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.net.Client;
import com.terryscape.net.PacketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// TODO: Separate this class into two?

@Singleton
public class WorldManagerImpl implements WorldManager {

    private static final Logger LOGGER = LogManager.getLogger(WorldManagerImpl.class);

    private final CacheLoader cacheLoader;

    private final PacketManager packetManager;

    private final Map<EntityIdentifier, EntityImpl> entities = new HashMap<>();

    private final Set<EntityIdentifier> entitiesToRemove = new HashSet<>();

    private final Map<WorldRegionCoordinate, WorldRegionImpl> regions = new HashMap<>();

    private final Set<EntityIdentifier> players = new HashSet<>();

    private final Map<EntityIdentifier, WorldRegionImpl> entityRegions = new HashMap<>();

    private final Map<EntityIdentifier, WorldRegionImpl> lastTickEntityRegions = new HashMap<>();

    @Inject
    public WorldManagerImpl(CacheLoader cacheLoader, PacketManager packetManager) {
        this.cacheLoader = cacheLoader;
        this.packetManager = packetManager;
    }

    @Override
    public void registerEntity(Entity entity) {
        var entityImpl = (EntityImpl) entity;
        entities.put(entity.getIdentifier(), entityImpl);

        entityImpl.onRegistered();

        // TODO: Swap to entity tags
        if (entityImpl.getPrefabType() == EntityPrefabType.PLAYER) {
            players.add(entity.getIdentifier());
        }

        LOGGER.info("Registered entity {}", entity.getIdentifier());
    }

    @Override
    public void registerEntity(Entity entity, WorldRegion worldRegion) {
        registerEntity(entity);
        registerEntityToWorldRegion(entity.getIdentifier(), worldRegion);
    }

    @Override
    public void deleteEntity(EntityIdentifier entityIdentifier) {
        // TODO: Swap to entity tags
        if (entities.get(entityIdentifier).getPrefabType() == EntityPrefabType.PLAYER) {
            players.remove(entityIdentifier);
        }

        removeEntityFromExistingRegion(entityIdentifier);
        entitiesToRemove.remove(entityIdentifier);

        LOGGER.info("Deleted entity {}", entityIdentifier);
    }

    @Override
    public Entity getEntity(EntityIdentifier entityIdentifier) {
        if (!entities.containsKey(entityIdentifier)) {
            throw new RuntimeException("Failed to find entity with identifier %s".formatted(entityIdentifier));
        }

        return entities.get(entityIdentifier);
    }

    @Override
    public void registerEntityToWorldRegion(EntityIdentifier entityIdentifier, WorldRegion worldRegion) {
        var entity = entities.get(entityIdentifier);
        if (entity == null) {
            // Handle the entity not being registered here as this can be called by components before the entity has been registered
            return;
        }

        var worldRegionImpl = ((WorldRegionImpl) worldRegion);
        if (worldRegionImpl.getEntitiesInRegion().contains(entityIdentifier)) {
            return;
        }

        removeEntityFromExistingRegion(entityIdentifier);
        worldRegionImpl.addEntity(entityIdentifier);
        entityRegions.put(entityIdentifier, worldRegionImpl);
    }

    @Override
    public WorldRegion getWorldRegionFromWorldCoordinate(WorldCoordinate worldCoordinate) {
        var worldRegionCoordinate = getWorldRegionCoordinateFromWorldCoordinate(worldCoordinate);

        return regions.get(worldRegionCoordinate);
    }

    public void initialise() {
        var stopwatch = Stopwatch.createStarted();

        for (var worldRegionCoordinate : cacheLoader.getAllWorldRegions()) {
            var region = new WorldRegionImpl(worldRegionCoordinate);
            regions.put(worldRegionCoordinate, region);
        }

        LOGGER.info("Initialised World in {} milliseconds.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public void tick() {
        entities.values().forEach(EntityImpl::tick);

        // TODO: Updating the players should be able to be parallelised pretty well
        players.parallelStream().forEach(this::updatePlayer);

        // TODO: This can also be parallelised but its pretty simple so a parallelStream might be enough
        regions.values().parallelStream().forEach(WorldRegionImpl::clearEntityTracking);

        entitiesToRemove.forEach(entities::remove);
        entitiesToRemove.clear();

        lastTickEntityRegions.clear();
        lastTickEntityRegions.putAll(entityRegions);
    }

    private WorldRegionCoordinate getWorldRegionCoordinateFromWorldCoordinate(WorldCoordinate worldCoordinate) {
        return new WorldRegionCoordinate(
            (int) Math.floor((double) worldCoordinate.getX() / Config.WORLD_REGION_SIZE.getX()),
            (int) Math.floor((double) worldCoordinate.getY() / Config.WORLD_REGION_SIZE.getY())
        );
    }

    private void removeEntityFromExistingRegion(EntityIdentifier entityIdentifier) {
        if (!entityRegions.containsKey(entityIdentifier)) {
            return;
        }

        var currentRegion = entityRegions.get(entityIdentifier);
        currentRegion.removeEntity(entityIdentifier);
    }

    private Set<WorldRegionImpl> calculateUpdateRegionsForWorldRegion(WorldRegionImpl worldRegion) {
        var set = new HashSet<WorldRegionImpl>();
        set.add(worldRegion);

        var neighbours = worldRegion.getWorldRegionCoordinate().getCardinalAndIntercardinalNeighbours();
        for (var neighbour : neighbours) {
            if (regions.containsKey(neighbour)) {
                set.add(regions.get(neighbour));
            }
        }

        return set;
    }

    // TODO: Sometimes we send packets that break the client. E.g. Register already registered entity or delete non-existent entity on client.
    // TODO: We should probably cache the packet writing. We shouldn't have to generate the npc write packets for each player as they will be the same.
    private void updatePlayer(EntityIdentifier entityIdentifier) {
        var client = entities.get(entityIdentifier).getComponentOrThrow(PlayerComponent.class).getClient();

        var currentRegionCoordinate = entityRegions.get(entityIdentifier);
        var newUpdateRegions = calculateUpdateRegionsForWorldRegion(currentRegionCoordinate);

        if (!lastTickEntityRegions.containsKey(entityIdentifier)) {
            // Add the local player first and then add all surrounding npcs and players
            doEntityAddedPacket(client, entityIdentifier);
            playerUpdateAddAllEntitiesFromRegions(client, newUpdateRegions, entityIdentifier);
            return;
        }

        // Update the local player first and then update all surrounding npcs and players
        doEntityUpdatedPacket(client, entityIdentifier);

        var lastRegionCoordinate = lastTickEntityRegions.get(entityIdentifier);
        var lastUpdateRegions = calculateUpdateRegionsForWorldRegion(lastRegionCoordinate);

        var noOverlap = Sets.intersection(newUpdateRegions, lastUpdateRegions).isEmpty();
        if (noOverlap) {
            playerUpdateRemoveAllEntitiesFromRegions(client, lastUpdateRegions, entityIdentifier);
            playerUpdateAddAllEntitiesFromRegions(client, newUpdateRegions, entityIdentifier);
            return;
        }

        playerUpdateRemoveAllEntitiesFromRegions(client, Sets.difference(lastUpdateRegions, newUpdateRegions), entityIdentifier);
        playerUpdateAddAllEntitiesFromRegions(client, Sets.difference(newUpdateRegions, lastUpdateRegions), entityIdentifier);
        playerUpdateDiffEntitiesFromRegions(client, Sets.intersection(newUpdateRegions, lastUpdateRegions), entityIdentifier);
    }

    private void playerUpdateAddAllEntitiesFromRegions(Client client, Set<WorldRegionImpl> regions, EntityIdentifier self) {
        regions.stream()
            .flatMap(worldRegion -> worldRegion.getEntitiesInRegion().stream())
            .filter(entityIdentifier -> !entityIdentifier.equals(self))
            .forEach(entityIdentifier -> doEntityAddedPacket(client, entityIdentifier));
    }

    private void playerUpdateRemoveAllEntitiesFromRegions(Client client, Set<WorldRegionImpl> regions, EntityIdentifier self) {
        regions.stream()
            .flatMap(worldRegion -> worldRegion.getEntitiesInRegionOrRemoved().stream())
            .filter(entityIdentifier -> !entityIdentifier.equals(self))
            .forEach(entityIdentifier -> doEntityRemovedPacket(client, entityIdentifier));
    }

    private void playerUpdateDiffEntitiesFromRegions(Client client, Set<WorldRegionImpl> regions, EntityIdentifier self) {
        var allEntitiesAdded = regions.stream()
            .flatMap(r -> r.getEntitiesAdded().stream())
            .filter(entityIdentifier -> !entityIdentifier.equals(self))
            .collect(Collectors.toSet());

        var allEntitiesRemoved = regions.stream()
            .flatMap(r -> r.getEntitiesRemoved().stream())
            .filter(entityIdentifier -> !entityIdentifier.equals(self))
            .collect(Collectors.toSet());

        var allEntities = regions.stream().
            flatMap(r -> r.getEntitiesInRegion().stream())
            .filter(entityIdentifier -> !entityIdentifier.equals(self))
            .collect(Collectors.toSet());

        Sets.difference(allEntitiesAdded, allEntitiesRemoved).forEach(identifier -> doEntityAddedPacket(client, identifier));
        Sets.difference(allEntitiesRemoved, allEntitiesAdded).forEach(identifier -> doEntityRemovedPacket(client, identifier));
        allEntities.forEach(identifier -> doEntityUpdatedPacket(client, identifier));
    }

    private void doEntityAddedPacket(Client client, EntityIdentifier entityIdentifier) {
        var packet = new EntityAddedOutgoingPacket().setEntity(entities.get(entityIdentifier));
        packetManager.send(client, packet);
    }

    private void doEntityRemovedPacket(Client client, EntityIdentifier entityIdentifier) {
        var packet = new EntityRemovedOutgoingPacket().setEntity(entities.get(entityIdentifier));
        packetManager.send(client, packet);
    }

    private void doEntityUpdatedPacket(Client client, EntityIdentifier entityIdentifier) {
        var packet = new EntityUpdatedOutgoingPacket().setEntity(entities.get(entityIdentifier));
        packetManager.send(client, packet);
    }

}
