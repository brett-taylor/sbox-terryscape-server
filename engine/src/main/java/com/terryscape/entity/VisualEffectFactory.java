package com.terryscape.entity;

import com.google.inject.Inject;
import com.terryscape.game.combat.ParticleComponent;
import com.google.inject.Singleton;
import com.terryscape.game.combat.ProjectileComponent;
import com.terryscape.world.WorldManager;

@Singleton
public class VisualEffectFactory {
    private static EntityPrefabFactory factory;
    private static WorldManager manager;

    @Inject
    public VisualEffectFactory (WorldManager manager, EntityPrefabFactory factory) {
        VisualEffectFactory.manager = manager;
        VisualEffectFactory.factory = factory;
    }

    public static ParticleComponent CreateParticle() {
        var particle = factory.createParticle();
        manager.registerEntity(particle.getEntity());
        return particle;
    }

    public static ProjectileComponent CreateProjectile() {
        var projectile = factory.createProjectile();
        manager.registerEntity(projectile.getEntity());
        return projectile;
    }
}
