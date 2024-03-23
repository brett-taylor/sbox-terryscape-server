package com.terryscape.entity;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.ComponentSystemManager;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.entity.event.EntityEvent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

public class EntityImpl implements Entity {

    private final ComponentSystemManager componentSystemManager;

    private final EntityIdentifier entityIdentifier;

    private final EntityPrefabType entityPrefabType;

    private final String entityPrefabIdentifier;

    private final List<BaseEntityComponent> components = new ArrayList<>();

    private final Map<Type, List<Consumer<? extends EntityEvent>>> entityEventConsumers = new HashMap<>();

    private boolean isValid;

    private boolean hasBeenRegistered;

    // TODO: Change EntityPrefabType to EntityTags and network those instead.
    public EntityImpl(ComponentSystemManager componentSystemManager, EntityIdentifier entityIdentifier, EntityPrefabType entityPrefabType, String entityPrefabIdentifier) {
        this.componentSystemManager = componentSystemManager;
        this.entityIdentifier = entityIdentifier;
        this.entityPrefabType = entityPrefabType;
        this.entityPrefabIdentifier = entityPrefabIdentifier;
    }

    @Override
    public EntityIdentifier getIdentifier() {
        return entityIdentifier;
    }

    @Override
    public EntityPrefabType getPrefabType() {
        return entityPrefabType;
    }

    @Override
    public String getPrefabIdentifier() {
        return entityPrefabIdentifier;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void delete() {
        isValid = false;
    }

    public void markAsValid() {
        isValid = true;
    }

    @Override
    public void addComponent(BaseEntityComponent component) {
        if (component.getEntity() != null) {
            throw new RuntimeException("Attempted to add a component owned by a different entity.");
        }

        component.setEntity(this);
        components.add(component);

        if (hasBeenRegistered) {
            component.onRegistered();
            componentSystemManager.notifyRegister(this, component);
        }
    }

    @Override
    public <T extends EntityComponent> boolean hasComponent(Class<T> componentType) {
        return components.stream().anyMatch(component -> componentType.isAssignableFrom(component.getClass()));
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

    @Override
    public <T extends EntityEvent> void subscribe(Class<T> eventType, Consumer<T> eventConsumer) {
        if (!entityEventConsumers.containsKey(eventType)) {
            entityEventConsumers.put(eventType, new ArrayList<>());
        }

        entityEventConsumers.get(eventType).add(eventConsumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EntityEvent> void invoke(Class<T> eventType, T entityEvent) {
        if (!entityEventConsumers.containsKey(eventType)) {
            return;
        }

        for (Consumer<? extends EntityEvent> consumer : entityEventConsumers.get(eventType)) {
            var typedConsumer = (Consumer<T>) consumer;
            typedConsumer.accept(entityEvent);
        }
    }

    public void writeEntityAddedPacket(OutputStream packet) {
        getIdentifier().writeToPacket(packet);
        OutgoingPacket.writeEnum(packet, getPrefabType());
        OutgoingPacket.writeString(packet, getPrefabIdentifier());

        for (var networkedComponent : getNetworkedComponents()) {
            OutgoingPacket.writeString(packet, networkedComponent.getComponentIdentifier());
            networkedComponent.writeEntityAddedPacket(packet);
        }

        components.forEach(entityComponent -> componentSystemManager.writeEntityAddedPacket(this, entityComponent, packet));
    }

    public void writeEntityUpdatedPacket(OutputStream packet) {
        getIdentifier().writeToPacket(packet);

        for (var networkedComponent : getNetworkedComponents()) {
            OutgoingPacket.writeString(packet, networkedComponent.getComponentIdentifier());
            networkedComponent.writeEntityUpdatedPacket(packet);
        }

        components.forEach(entityComponent -> componentSystemManager.writeEntityUpdatedPacket(this, entityComponent, packet));
    }

    public void writeEntityRemovedPacket(OutputStream packet) {
        getIdentifier().writeToPacket(packet);
    }

    public void onRegistered() {
        hasBeenRegistered = true;

        components.forEach(component -> {
            component.onRegistered();
            componentSystemManager.notifyRegister(this, component);
        });
    }

    public void onDeleted() {
        components.forEach(component -> {
            component.onDeleted();
            componentSystemManager.notifyDeleted(this, component);
        });
    }

    public void tick() {
        components.forEach(component -> {
            component.tick();
            componentSystemManager.notifyTick(this, component);
        });

        var componentsToRemove = components.stream().filter(BaseEntityComponent::shouldDelete).toList();
        componentsToRemove.forEach(this::removeComponentImmediate);
    }

    private List<NetworkedEntityComponent> getNetworkedComponents() {
        return components.stream()
            .filter(component -> NetworkedEntityComponent.class.isAssignableFrom(component.getClass()))
            .map(NetworkedEntityComponent.class::cast)
            .toList();
    }

    private void removeComponentImmediate(BaseEntityComponent component) {
        components.remove(component);
    }
}
