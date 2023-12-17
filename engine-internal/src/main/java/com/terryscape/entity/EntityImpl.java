package com.terryscape.entity;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.entity.event.EntityEvent;
import com.terryscape.event.EntityEventSystem;
import com.terryscape.event.EntityEventSystemImpl;
import com.terryscape.event.EventSystemImpl;
import com.terryscape.net.OutgoingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

public class EntityImpl implements Entity {

    private static final Logger LOGGER = LogManager.getLogger(EntityImpl.class);
    private static final EntityEventSystem entityEventSystem = new EntityEventSystemImpl();

    private final EntityIdentifier entityIdentifier;

    private final EntityPrefabType entityPrefabType;

    private final String entityPrefabIdentifier;

    private final List<BaseEntityComponent> components = new ArrayList<>();

    private boolean hasBeenRegistered = false;

    private boolean hasSpawned = false;

    private boolean isValid = true;

    public EntityImpl(EntityIdentifier entityIdentifier, EntityPrefabType entityPrefabType, String entityPrefabIdentifier) {
        this.entityIdentifier = entityIdentifier;
        this.entityPrefabType = entityPrefabType;
        this.entityPrefabIdentifier = entityPrefabIdentifier;
    }

    @Override
    public EntityIdentifier getIdentifier() {
        return entityIdentifier;
    }

    @Override
    public EntityPrefabType getEntityPrefabType() {
        return entityPrefabType;
    }

    @Override
    public String getEntityPrefabIdentifier() {
        return entityPrefabIdentifier;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addComponent(BaseEntityComponent component) {
        if (!component.getEntity().equals(this)) {
            throw new RuntimeException("Attempted to add a component owned by a different entity.");
        }

        components.add(component);

        if (hasBeenRegistered) {
            component.onRegister();
        }

        if (hasSpawned) {
            component.onSpawn();
        }
    }

    @Override
    public <T extends EntityComponent> Optional<T> getComponent(Class<T> componentType) {
        for (var component : components) {
            if (componentType.isAssignableFrom(component.getClass())) {
                return Optional.of(componentType.cast(component));
            }
        }

        return Optional.empty();
    }

    @Override
    public <T extends EntityComponent> T getComponentOrThrow(Class<T> componentType) {
        return getComponent(componentType)
            .orElseThrow(() -> new RuntimeException("Attempted to get a component that does not exist on the entity."));
    }

    public void writeEntityAddedPacket(OutputStream packet) {
        getIdentifier().writeToPacket(packet);
        OutgoingPacket.writeEnum(packet, getEntityPrefabType());
        OutgoingPacket.writeString(packet, getEntityPrefabIdentifier());

        for (var networkedComponent : getNetworkedComponents()) {
            OutgoingPacket.writeString(packet, networkedComponent.getComponentIdentifier());
            networkedComponent.writeEntityAddedPacket(packet);
        }
    }

    public void writeEntityUpdatedPacket(OutputStream packet) {
        getIdentifier().writeToPacket(packet);

        for (var networkedComponent : getNetworkedComponents()) {
            OutgoingPacket.writeString(packet, networkedComponent.getComponentIdentifier());
            networkedComponent.writeEntityUpdatedPacket(packet);
        }
    }

    public void writeEntityRemovedPacket(OutputStream packet) {
        getIdentifier().writeToPacket(packet);
    }

    public void onRegister() {
        hasBeenRegistered = true;

        components.forEach(BaseEntityComponent::onRegister);
    }

    public void onSpawn() {
        hasSpawned = true;

        components.forEach(BaseEntityComponent::onSpawn);
    }

    public void tick() {
        components.forEach(BaseEntityComponent::tick);
    }

    public void delete() {
        isValid = false;
        components.forEach(entityEventSystem::onComponentDestroy);
        entityEventSystem.onEntityDestroy(this);
    }

    private List<NetworkedEntityComponent> getNetworkedComponents() {
        return components.stream()
            .filter(component -> NetworkedEntityComponent.class.isAssignableFrom(component.getClass()))
            .map(NetworkedEntityComponent.class::cast)
            .toList();
    }

    @Override
    public <T extends EntityEvent> void subscribe(EntityComponent broadcaster, Class<T> event, EntityComponent component, Consumer<T> method) {
        entityEventSystem.subscribe(broadcaster.getEntity(), event, component, method);
    }

    @Override
    public <T extends EntityEvent> void unsubscribe(EntityComponent broadcaster, Class<T> event, EntityComponent component) {
        entityEventSystem.unsubscribe(broadcaster.getEntity(), event, component);
    }

    @Override
    public <T extends EntityEvent> void invoke(T event) {
        entityEventSystem.invoke(this, event);
    }
}
