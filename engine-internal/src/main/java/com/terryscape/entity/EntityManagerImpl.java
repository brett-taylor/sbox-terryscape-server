package com.terryscape.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.packet.EntityAddedOutgoingPacket;
import com.terryscape.entity.packet.EntityRemovedOutgoingPacket;
import com.terryscape.entity.packet.EntityUpdatedOutgoingPacket;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.net.Client;
import com.terryscape.net.PacketManager;
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
    public void registerEntity(Entity entity) {
        entitiesToRegisterNextTick.put(entity.getIdentifier(), (EntityImpl) entity);
    }

    @Override
    public void deleteEntity(EntityIdentifier entityIdentifier) {
        var entity = entities.get(entityIdentifier);

        if (entity == null) {
            LOGGER.error("Attempted to delete an entity that no longer exists.");
            return;
        }

        entitiesToUnregisterNextTick.put(entityIdentifier, entity);
    }

    @Override
    public void sendInitialUpdate(Client client) {
        entities.values().stream()
            .map(entity -> new EntityAddedOutgoingPacket().setEntity(entity))
            .forEach(packet -> packetManager.send(client, packet));
    }

    @Override
    public NpcComponent getNpc(EntityIdentifier entityIdentifier) {
        if (!entities.containsKey(entityIdentifier)) {
            throw new RuntimeException("Failed to find entity with identifier %s".formatted(entityIdentifier));
        }

        return entities.get(entityIdentifier).getComponentOrThrow(NpcComponent.class);
    }

    public void tick() {
        entitiesToUnregisterNextTick.values().forEach(this::unregisterSingleEntity);
        entitiesToUnregisterNextTick.clear();

        entitiesToRegisterNextTick.values().forEach(this::registerSingleEntity);
        entitiesToRegisterNextTick.clear();

        entities.values().forEach(this::tickSingleEntity);
    }

    private void registerSingleEntity(EntityImpl entity) {
        entities.put(entity.getIdentifier(), entity);

        entity.onRegister();

        var packet = new EntityAddedOutgoingPacket().setEntity(entity);
        packetManager.broadcast(packet);

        entity.onSpawn();

        LOGGER.info("Registered Entity {}", entity.getIdentifier());
    }

    private void unregisterSingleEntity(EntityImpl entity) {
        entity.delete();

        entities.remove(entity.getIdentifier());

        var packet = new EntityRemovedOutgoingPacket().setEntity(entity);
        packetManager.broadcast(packet);

        LOGGER.info("Unregistered Entity {}", entity.getIdentifier());
    }

    private void tickSingleEntity(EntityImpl entity) {
        entity.tick();

        var updatePacket = new EntityUpdatedOutgoingPacket().setEntity(entity);
        packetManager.broadcast(updatePacket);
    }
}
