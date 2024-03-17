package com.terryscape.game.projectile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.projectile.ProjectileDefinition;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.entity.EntityManager;

import java.util.function.Consumer;

@Singleton
public class ProjectileFactoryImpl implements ProjectileFactory {

    private final EntityPrefabFactory entityPrefabFactory;

    private final EntityManager entityManager;

    private final CacheLoader cacheLoader;

    @Inject
    public ProjectileFactoryImpl(EntityPrefabFactory entityPrefabFactory, EntityManager entityManager, CacheLoader cacheLoader) {
        this.entityPrefabFactory = entityPrefabFactory;
        this.entityManager = entityManager;
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

        entityManager.registerEntity(projectile);
        return projectileComponent;
    }
}
