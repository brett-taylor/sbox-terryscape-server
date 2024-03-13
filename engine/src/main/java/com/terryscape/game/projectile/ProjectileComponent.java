package com.terryscape.game.projectile;

import com.terryscape.cache.projectile.ProjectileDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.NetworkedEntityComponent;

public interface ProjectileComponent extends NetworkedEntityComponent {

    ProjectileDefinition getProjectileDefinition();

    void setEntityTarget(Entity entityTarget);

    void setEntitySource(Entity entitySource);

    void setLifeSpan(int lifeSpan);

}
