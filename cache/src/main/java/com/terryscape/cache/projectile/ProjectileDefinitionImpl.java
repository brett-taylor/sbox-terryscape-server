package com.terryscape.cache.projectile;

public class ProjectileDefinitionImpl implements ProjectileDefinition {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    public ProjectileDefinitionImpl setId(String id) {
        this.id = id;
        return this;
    }
}
