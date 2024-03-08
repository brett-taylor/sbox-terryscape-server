package com.terryscape.entity.component;

import com.terryscape.entity.Entity;

public abstract class BaseEntityComponent implements EntityComponent {

    private final Entity entity;

    private boolean isRemoved = false;

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

    public void delete() {
        isRemoved = true;
    }

    public boolean shouldDelete() {
        return isRemoved;
    }
}
