package com.terryscape.entity;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.EntityComponent;

public interface Entity {

    EntityIdentifier getIdentifier();

    EntityPrefabType getEntityPrefabType();

    void addComponent(BaseEntityComponent component);

    <T extends EntityComponent> T getComponentOrThrow(Class<T> componentType);

}
