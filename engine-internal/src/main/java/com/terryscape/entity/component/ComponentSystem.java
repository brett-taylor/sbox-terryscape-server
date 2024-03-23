package com.terryscape.entity.component;

import com.terryscape.entity.Entity;

import java.io.OutputStream;

public abstract class ComponentSystem<T extends BaseEntityComponent> {

    public abstract Class<T> forComponentType();

    public void onRegistered(Entity entity, T component) {
    }

    public void onTick(Entity entity, T component) {
    }

    public void onDeleted(Entity entity, T component) {

    }

    public boolean isNetworked() {
        return false;
    }

    public String getComponentNetworkIdentifier() {
        return "";
    }

    public void writeEntityAddedPacket(Entity entity, T component, OutputStream packet) {
    }

    public void writeEntityUpdatedPacket(Entity entity, T component, OutputStream packet) {
    }
}

