package com.terryscape.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityIdentifier;
import com.terryscape.entity.EntityImpl;
import com.terryscape.entity.packet.EntityAddedOutgoingPacket;
import com.terryscape.entity.packet.EntityRemovedOutgoingPacket;
import com.terryscape.entity.packet.EntityUpdatedOutgoingPacket;
import com.terryscape.net.Client;
import com.terryscape.net.PacketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Singleton
public class WorldManagerImpl implements WorldManager {

    private static final Logger LOGGER = LogManager.getLogger(WorldManagerImpl.class);

    private final PacketManager packetManager;

    private final Map<EntityIdentifier, EntityImpl> entitiesToRegisterNextTick;
    private final Map<EntityIdentifier, EntityImpl> entities;

    @Inject
    public WorldManagerImpl(PacketManager packetManager) {
        this.packetManager = packetManager;

        entities = new HashMap<>();
        entitiesToRegisterNextTick = new ConcurrentHashMap<>();
    }

    @Override
    public void registerEntity(Entity entity) {
        EntityImpl entityImp = (EntityImpl) entity;
        entitiesToRegisterNextTick.put(entity.getIdentifier(), entityImp);
        entityImp.markAsValid();
    }

    @Override
    public void sendInitialUpdate(Client client) {
        entities.values().stream()
            .map(entity -> new EntityAddedOutgoingPacket().setEntity(entity))
            .forEach(packet -> packetManager.send(client, packet));
    }

    @Override
    public Entity getEntity(EntityIdentifier entityIdentifier) {
        if (!entities.containsKey(entityIdentifier)) {
            throw new RuntimeException("Failed to find entity with identifier %s".formatted(entityIdentifier));
        }

        return entities.get(entityIdentifier);
    }

    public void tick() {
        entitiesToRegisterNextTick.values().forEach(this::registerSingleEntity);
        entitiesToRegisterNextTick.clear();

        entities.values().forEach(this::tickSingleEntity);

        var entitiesToRemove = entities.values().stream().filter(Predicate.not(EntityImpl::isValid)).toList();
        entitiesToRemove.forEach(this::unregisterSingleEntity);
    }

    private void registerSingleEntity(EntityImpl entity) {
        entities.put(entity.getIdentifier(), entity);

        entity.onRegistered();

        var packet = new EntityAddedOutgoingPacket().setEntity(entity);
        packetManager.broadcast(packet);

        LOGGER.debug("Registered {} {}", entity.getPrefabType(), entity.getIdentifier());
    }

    private void unregisterSingleEntity(EntityImpl entity) {
        entities.remove(entity.getIdentifier());

        var packet = new EntityRemovedOutgoingPacket().setEntity(entity);
        packetManager.broadcast(packet);

        LOGGER.debug("Unregistered {} {}", entity.getPrefabType(), entity.getIdentifier());
    }

    private void tickSingleEntity(EntityImpl entity) {
        try {
            entity.tick();

            var updatePacket = new EntityUpdatedOutgoingPacket().setEntity(entity);
            packetManager.broadcast(updatePacket);
        } catch (Exception e) {
            LOGGER.error("Failed updating Entity {} {}", entity.getPrefabType(), entity.getIdentifier());
            LOGGER.error(e);
        }
    }

}
