package com.terryscape.game.projectile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.projectile.ProjectileDefinition;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.world.WorldManager;

import java.util.function.Consumer;

@Singleton
public class ProjectileFactoryImpl implements ProjectileFactory {

    private final EntityPrefabFactory entityPrefabFactory;

    private final WorldManager worldManager;

    private final CacheLoader cacheLoader;

    @Inject
    public ProjectileFactoryImpl(EntityPrefabFactory entityPrefabFactory, WorldManager worldManager, CacheLoader cacheLoader) {
        this.entityPrefabFactory = entityPrefabFactory;
        this.worldManager = worldManager;
        this.cacheLoader = cacheLoader;
    }

    @Override
    public ProjectileComponent createRegisteredProjectile(String projectileId, Consumer<ProjectileComponent> projectileSetup) {
        return createRegisteredProjectile(cacheLoader.getProjectileDefinition(projectileId), projectileSetup);
    }

    @Override
    public ProjectileComponent createRegisteredProjectile(ProjectileDefinition projectileDefinition, Consumer<ProjectileComponent> projectileSetup) {
        var projectile = entityPrefabFactory.createProjectilePrefab(projectileDefinition);
        var projectileComponent = projectile.getComponentOrThrow(ProjectileComponent.class);

        projectileSetup.accept(projectileComponent);

        worldManager.registerEntity(projectile);
        return projectileComponent;
    }
}
