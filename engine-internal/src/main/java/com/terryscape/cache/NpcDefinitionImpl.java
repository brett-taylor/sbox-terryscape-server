package com.terryscape.cache;

public class NpcDefinitionImpl implements NpcDefinition {

    private String id;

    private String name;

    private String description;

    @Override
    public String getId() {
        return id;
    }

    public NpcDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public NpcDefinitionImpl setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public NpcDefinitionImpl setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "NpcDefinition(id=%s)".formatted(id);
    }
}
