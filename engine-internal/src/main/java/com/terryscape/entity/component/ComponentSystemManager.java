package com.terryscape.entity.component;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.net.OutgoingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

@Singleton
public class ComponentSystemManager {

    private static final Logger LOGGER = LogManager.getLogger(ComponentSystemManager.class);

    private final HashMap<Class<? extends BaseEntityComponent>, ComponentSystem<?>> componentSystems;

    @Inject
    @SuppressWarnings("rawtypes")
    public ComponentSystemManager(Set<ComponentSystem> componentSystems) {
        this.componentSystems = new HashMap<>();
        componentSystems.forEach(this::registerEntityComponentSystem);
        LOGGER.info("Registered {} Component Systems.", componentSystems.size());
    }

    public void notifyRegister(Entity entity, BaseEntityComponent component) {
        var system = getEntityComponentSystemForType(component);
        if (system != null) {
            system.onRegistered(entity, component);
        }
    }

    public void notifyTick(Entity entity, BaseEntityComponent component) {
        var system = getEntityComponentSystemForType(component);
        if (system != null) {
            system.onTick(entity, component);
        }
    }

    public void notifyDeleted(Entity entity, BaseEntityComponent component) {
        var system = getEntityComponentSystemForType(component);
        if (system != null) {
            system.onDeleted(entity, component);
        }
    }

    public void writeEntityAddedPacket(Entity entity, BaseEntityComponent component, OutputStream packet) {
        var system = getEntityComponentSystemForType(component);
        if (system != null && system.isNetworked()) {
            OutgoingPacket.writeString(packet, system.getComponentNetworkIdentifier());
            system.writeEntityAddedPacket(entity, component, packet);
        }
    }

    public void writeEntityUpdatedPacket(Entity entity, BaseEntityComponent component, OutputStream packet) {
        var system = getEntityComponentSystemForType(component);
        if (system != null && system.isNetworked()) {
            OutgoingPacket.writeString(packet, system.getComponentNetworkIdentifier());
            system.writeEntityUpdatedPacket(entity, component, packet);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseEntityComponent> ComponentSystem<T> getEntityComponentSystemForType(T entityComponentType) {
        var result = componentSystems.get(entityComponentType.getClass());
        if (result != null) {
            return (ComponentSystem<T>) result;
        }

        return null;
    }

    private void registerEntityComponentSystem(ComponentSystem<? extends BaseEntityComponent> componentSystem) {
        if (componentSystems.containsKey(componentSystem.forComponentType())) {
            throw new IllegalArgumentException(
                "Component type %s already has a entity component system registered".formatted(componentSystem.forComponentType())
            );
        }

        componentSystems.put(componentSystem.forComponentType(), componentSystem);
    }
}

