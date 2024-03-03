package com.terryscape.cache.item;

import java.util.Optional;

public class ItemDefinitionImpl implements ItemDefinition {

    private String id;

    private String name;

    private String description;

    private boolean stackable;

    private EquipItemDefinition equipItemDefinition;

    @Override
    public String getId() {
        return id;
    }

    public ItemDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public ItemDefinitionImpl setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public ItemDefinitionImpl setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean isStackable() {
        return stackable;
    }

    public ItemDefinitionImpl setStackable(boolean stackable) {
        this.stackable = stackable;
        return this;
    }

    @Override
    public Optional<EquipItemDefinition> getEquipDefinition() {
        return Optional.ofNullable(equipItemDefinition);
    }

    @Override
    public EquipItemDefinition getEquipDefinitionOrThrow() {
        if (equipItemDefinition == null) {
            throw new RuntimeException("No equip information for item %s".formatted(getId()));
        }

        return equipItemDefinition;
    }

    public ItemDefinitionImpl setEquipItemDefinition(EquipItemDefinition equipItemDefinition) {
        this.equipItemDefinition = equipItemDefinition;
        return this;
    }

    @Override
    public String toString() {
        return "ItemDefinition(id=%s)".formatted(id);
    }
}
