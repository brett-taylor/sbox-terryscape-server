package com.terryscape.entity.component;

import com.terryscape.entity.Entity;

public abstract class BaseEntityComponent implements EntityComponent {

    private Entity entity;

    private boolean isRemoved = false;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void tick() {
    }

    public void onRegistered() {
    }

    public void onDeleted() {
    }

    public void removeFromEntity() {
        isRemoved = true;
    }

    public boolean shouldDelete() {
        return isRemoved;
    }
}
