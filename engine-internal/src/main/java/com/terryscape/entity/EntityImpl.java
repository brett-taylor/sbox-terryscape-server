package com.terryscape.entity;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.entity.event.EntityEvent;
import com.terryscape.net.OutgoingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

public class EntityImpl implements Entity {

    private static final Logger LOGGER = LogManager.getLogger(EntityImpl.class);

    private final EntityIdentifier entityIdentifier;

    private final EntityPrefabType entityPrefabType;

    private final String entityPrefabIdentifier;

    private final List<BaseEntityComponent> components = new ArrayList<>();

    private final Map<Type, List<Consumer<? extends EntityEvent>>> entityEventConsumers = new HashMap<>();

    private boolean hasBeenRegistered = false;

    // TODO: Change EntityPrefabType to EntityTags and network those instead.
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
    public EntityPrefabType getPrefabType() {
        return entityPrefabType;
    }

    @Override
    public String getPrefabIdentifier() {
        return entityPrefabIdentifier;
    }

    @Override
    public void addComponent(BaseEntityComponent component) {
        if (!component.getEntity().equals(this)) {
            throw new RuntimeException("Attempted to add a component owned by a different entity.");
        }

        components.add(component);

        if (hasBeenRegistered) {
            component.onRegistered();
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

    @Override
    public <T extends EntityEvent> void invoke(Class<T> eventType, T entityEvent) {
        if (!entityEventConsumers.containsKey(eventType)) {
            LOGGER.error("Attempted to invoke entity event %s that has no subscribed listeners".formatted(eventType.getName()));
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

    public void onRegistered() {
        hasBeenRegistered = true;

        components.forEach(BaseEntityComponent::onRegistered);
    }

    public void tick() {
        components.forEach(BaseEntityComponent::tick);

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
