package com.terryscape.entity;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EntityImpl implements Entity {

    private final EntityIdentifier entityIdentifier;

    private final EntityPrefabType entityPrefabType;

    private final String entityPrefabIdentifier;

    private final List<BaseEntityComponent> components = new ArrayList<>();

    private boolean hasBeenRegistered = false;

    private boolean hasSpawned = false;

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
    public <T extends EntityComponent> T getComponentOrThrow(Class<T> componentType) {
        for (var component : components) {
            if (componentType.isAssignableFrom(component.getClass())) {
                return componentType.cast(component);
            }
        }

        throw new RuntimeException("Attempted to get a component that does not exist on the entity.");
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

    private List<NetworkedEntityComponent> getNetworkedComponents() {
        return components.stream()
            .filter(component -> NetworkedEntityComponent.class.isAssignableFrom(component.getClass()))
            .map(NetworkedEntityComponent.class::cast)
            .toList();
    }
}
