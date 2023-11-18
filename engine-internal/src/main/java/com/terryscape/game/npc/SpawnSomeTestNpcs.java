package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityManager;
import com.terryscape.event.EventListener;
import com.terryscape.event.type.OnGameStartedEvent;
import com.terryscape.world.WorldCoordinate;

@Singleton
public class SpawnSomeTestNpcs implements EventListener {

    private final EntityManager entityManager;

    private final NpcFactory npcFactory;

    private final CacheLoader cacheLoader;

    @Inject
    public SpawnSomeTestNpcs(EntityManager entityManager, NpcFactory npcFactory, CacheLoader cacheLoader) {
        this.entityManager = entityManager;
        this.npcFactory = npcFactory;
        this.cacheLoader = cacheLoader;
    }

    public void onWorldStartedEvent(OnGameStartedEvent event) {
        var npc1 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("citizen_man"));
        npc1.addComponent(new WanderMovementComponent(npc1));
        npc1.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);

        var npc2 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("citizen_man"));
        npc2.addComponent(new WanderMovementComponent(npc2));
        npc2.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);

        var npc3 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("citizen_man"));
        npc3.addComponent(new WanderMovementComponent(npc3));
        npc3.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);

        var npc4 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("citizen_woman"));
        npc4.addComponent(new WanderMovementComponent(npc4));
        npc4.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);

        var npc5 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("citizen_woman"));
        npc5.addComponent(new WanderMovementComponent(npc5));
        npc5.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);

        var npc6 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("citizen_woman"));
        npc6.addComponent(new WanderMovementComponent(npc6));
        npc6.getComponentOrThrow(WanderMovementComponent.class).startWander(new WorldCoordinate(10, 10), 9);

        entityManager.registerEntity(npc1);
        entityManager.registerEntity(npc2);
        entityManager.registerEntity(npc3);
        entityManager.registerEntity(npc4);
        entityManager.registerEntity(npc5);
        entityManager.registerEntity(npc6);
    }

}
