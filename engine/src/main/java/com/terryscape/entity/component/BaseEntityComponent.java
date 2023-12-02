package com.terryscape.entity.component;

import com.terryscape.entity.Entity;

public abstract class BaseEntityComponent implements EntityComponent {

    private final Entity entity;

    private boolean enabled;

    public BaseEntityComponent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    /**
     * Called when the entity has been registered to the world but has not yet spawned.
     * If the entity has already been registered, then it will be called straight away.
     */
    public void onRegister() {
    }

    /**
     * Called when the entity has been spawned into the world.
     * If the entity has already been spawned, then it will be called straight away.
     */
    public void onSpawn() {
    }

    public void tick() {
    }
}
