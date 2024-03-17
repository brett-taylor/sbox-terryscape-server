package com.terryscape.cache.object;

public class ObjectDefinitionImpl implements ObjectDefinition {

    private String id;

    private String name;

    private String description;

    private boolean interactable;

    @Override
    public String getId() {
        return id;
    }

    public ObjectDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public ObjectDefinitionImpl setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public ObjectDefinitionImpl setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean isInteractable() {
        return interactable;
    }

    public ObjectDefinitionImpl setInteractable(boolean interactable) {
        this.interactable = interactable;
        return this;
    }
}
