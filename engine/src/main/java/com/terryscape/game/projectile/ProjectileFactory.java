package com.terryscape.game.projectile;

import com.terryscape.cache.projectile.ProjectileDefinition;

import java.util.function.Consumer;

public interface ProjectileFactory {

    ProjectileComponent createRegisteredProjectile(String projectileDefinitionId, Consumer<ProjectileComponent> projectileSetup);

    ProjectileComponent createRegisteredProjectile(ProjectileDefinition projectileDefinition, Consumer<ProjectileComponent> projectileSetup);

}
