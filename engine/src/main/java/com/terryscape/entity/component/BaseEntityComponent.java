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

    public void tick() {
    }

    public void onRegistered() {
    }
}
