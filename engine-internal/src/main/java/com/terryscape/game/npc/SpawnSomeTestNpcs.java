package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityManager;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.world.WorldCoordinate;

@Singleton
public class SpawnSomeTestNpcs {

    private final EntityManager entityManager;

    private final NpcFactory npcFactory;

    private final CacheLoader cacheLoader;

    @Inject
    public SpawnSomeTestNpcs(EntityManager entityManager, NpcFactory npcFactory, CacheLoader cacheLoader, EventSystem eventSystem) {
        this.entityManager = entityManager;
        this.npcFactory = npcFactory;
        this.cacheLoader = cacheLoader;

        eventSystem.subscribe(OnGameStartedSystemEvent.class, this::onGameStartedEvent);
    }

    public void onGameStartedEvent(OnGameStartedSystemEvent event) {
        for (int i = 0; i < 4; i++) {
            var npc1 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin"));
            npc1.addComponent(new WanderMovementComponent(npc1));
            npc1.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);
            entityManager.registerEntity(npc1);
        }

        for (int i = 0; i < 4; i++) {
            var npc1 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin_warrior"));
            npc1.addComponent(new WanderMovementComponent(npc1));
            npc1.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);
            entityManager.registerEntity(npc1);
        }

        var npc1 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin_shaman"));
        npc1.addComponent(new WanderMovementComponent(npc1));
        npc1.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);
        entityManager.registerEntity(npc1);

        var npc2 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin_chief"));
        npc2.addComponent(new WanderMovementComponent(npc2));
        npc2.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);
        entityManager.registerEntity(npc2);
    }

}
