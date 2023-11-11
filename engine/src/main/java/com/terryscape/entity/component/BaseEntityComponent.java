package com.terryscape.entity.component;

import com.terryscape.entity.Entity;

public abstract class BaseEntityComponent implements EntityComponent {

    private final Entity entity;

    public BaseEntityComponent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * Called when the component is added to an entity
     */
    public void onAdded() {
    }

    /**
     * Called when the entity has been registered to the world but has not yet spawned.
     * If the component is added during the lifecycle of the entity rather than at the start, this will not be called.
     */
    public void onRegister() {
    }

    /**
     * Called when the entity has been spawned into the world.
     * If the component is added during the lifecycle of the entity rather than at the start, this will not be called.
     */
    public void onSpawn() {
    }

    public void tick() {
    }
}
