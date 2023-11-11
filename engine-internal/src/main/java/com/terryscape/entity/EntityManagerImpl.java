package com.terryscape.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.player.Player;
import com.terryscape.net.PacketManager;
import com.terryscape.net.packet.outgoing.EntityAddedOutgoingPacket;
import com.terryscape.net.packet.outgoing.EntityRemovedOutgoingPacket;
import com.terryscape.net.packet.outgoing.EntityUpdatedOutgoingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class EntityManagerImpl implements EntityManager {

    private static final Logger LOGGER = LogManager.getLogger(EntityManagerImpl.class);

    private final PacketManager packetManager;

    private final Map<EntityIdentifier, EntityImpl> entitiesToRegisterNextTick;
    private final Map<EntityIdentifier, EntityImpl> entitiesToUnregisterNextTick;

    private final Map<EntityIdentifier, EntityImpl> entities;

    @Inject
    public EntityManagerImpl(PacketManager packetManager) {
        this.packetManager = packetManager;

        entities = new HashMap<>();
        entitiesToRegisterNextTick = new ConcurrentHashMap<>();
        entitiesToUnregisterNextTick = new ConcurrentHashMap<>();
    }

    @Override
    public void sendPlayerInitialUpdate(Player player) {
        if (player.getIdentifier().isEmpty()) {
            LOGGER.error("A player should be registered before sending an initial update.");
            return;
        }

        entities.values().stream()
            .filter(entity -> !entity.getIdentifier().equals(player.getIdentifier()))
            .map(entity -> new EntityAddedOutgoingPacket().setEntity(entity))
            .forEach(packet -> packetManager.send(player.getClient(), packet));
    }

    @Override
    public void registerEntity(Entity entity) {
        if (entity.getIdentifier().isPresent()) {
            LOGGER.error("Entity already has an identifier, has it been registered already? The player will not be registered.");
            return;
        }

        var entityImpl = (EntityImpl) entity;

        var newEntityIdentifier = EntityIdentifier.randomIdentifier();
        entityImpl.setEntityIdentifier(newEntityIdentifier);

        entitiesToRegisterNextTick.put(newEntityIdentifier, entityImpl);
    }

    @Override
    public void unregisterEntity(Entity entity) {
        if (entity.getIdentifier().isEmpty()) {
            LOGGER.error("Attempted to unregister an entity without an identifier, was it ever registered?.");
            return;
        }

        var entityImpl = (EntityImpl) entity;
        entitiesToUnregisterNextTick.put(entity.getIdentifier().get(), entityImpl);
    }

    public void tick() {
        entitiesToUnregisterNextTick.values().forEach(this::unregisterSingleEntity);
        entitiesToUnregisterNextTick.clear();

        entitiesToRegisterNextTick.values().forEach(this::registerSingleEntity);
        entitiesToRegisterNextTick.clear();

        entities.values().forEach(this::tickSingleEntity);
    }

    private void registerSingleEntity(EntityImpl entity) {
        var identifier = entity.getIdentifier().orElseThrow();

        entities.put(identifier, entity);

        entity.register();

        var packet = new EntityAddedOutgoingPacket().setEntity(entity);
        packetManager.broadcast(packet);

        entity.spawn();

        LOGGER.info("Registered Entity {}", identifier);
    }

    private void unregisterSingleEntity(EntityImpl entity) {
        var identifier = entity.getIdentifier().orElseThrow();

        entities.remove(entity.getIdentifier().get());

        var packet = new EntityRemovedOutgoingPacket().setEntity(entity);
        packetManager.broadcast(packet);

        LOGGER.info("Unregistered Entity {}", identifier);
    }

    private void tickSingleEntity(EntityImpl entity) {
        entity.tick();

        var updatePacket = new EntityUpdatedOutgoingPacket().setEntity(entity);
        packetManager.broadcast(updatePacket);
    }
}
