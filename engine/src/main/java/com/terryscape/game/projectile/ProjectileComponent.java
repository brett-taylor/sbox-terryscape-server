package com.terryscape.game.projectile;

import com.terryscape.cache.projectile.ProjectileDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;

public class ProjectileComponent extends BaseEntityComponent {

    private final ProjectileDefinition projectileDefinition;

    private Entity entitySource;

    private Entity entityTarget;

    private int lifeSpan;

    private int lifeSpanLeft;

    public ProjectileComponent(ProjectileDefinition projectileDefinition) {
        this.projectileDefinition = projectileDefinition;
    }

    public ProjectileDefinition getProjectileDefinition() {
        return projectileDefinition;
    }

    public Entity getEntitySource() {
        return entitySource;
    }

    public void setEntitySource(Entity entitySource) {
        this.entitySource = entitySource;
    }

    public Entity getEntityTarget() {
        return entityTarget;
    }

    public void setEntityTarget(Entity entityTarget) {
        this.entityTarget = entityTarget;
    }

    public int getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public int getLifeSpanLeft() {
        return lifeSpanLeft;
    }

    public void decrementLifeSpanLeft() {
        this.lifeSpanLeft -= 1;
    }

}
