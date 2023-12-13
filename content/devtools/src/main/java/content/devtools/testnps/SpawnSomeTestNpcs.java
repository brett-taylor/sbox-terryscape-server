package content.devtools.testnps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityManager;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcFactory;
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
        var spawnCoordinate = new WorldCoordinate(10, 10);
        var wanderRadius = 9;

        for (int i = 0; i < 4; i++) {
            var npc1 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin"));
            npc1.getComponentOrThrow(MovementComponent.class).teleport(spawnCoordinate);
            npc1.addComponent(new WanderMovementComponent(npc1, wanderRadius));
            entityManager.registerEntity(npc1);
        }

        for (int i = 0; i < 4; i++) {
            var npc1 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin_warrior"));
            npc1.getComponentOrThrow(MovementComponent.class).teleport(spawnCoordinate);
            npc1.addComponent(new WanderMovementComponent(npc1, wanderRadius));
            entityManager.registerEntity(npc1);
        }

        var npc1 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin_shaman"));
        npc1.getComponentOrThrow(MovementComponent.class).teleport(spawnCoordinate);
        npc1.addComponent(new WanderMovementComponent(npc1, wanderRadius));
        entityManager.registerEntity(npc1);

        var npc2 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin_chief"));
        npc2.getComponentOrThrow(MovementComponent.class).teleport(spawnCoordinate);
        npc2.addComponent(new WanderMovementComponent(npc2, wanderRadius));
        entityManager.registerEntity(npc2);

        var staticNpc1 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin_warrior"));
        staticNpc1.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(6, 7));
        entityManager.registerEntity(staticNpc1);

        var staticNpc2 = npcFactory.createUnregisteredNpc(cacheLoader.getNpc("goblin_warrior"));
        staticNpc2.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(7, 6));
        entityManager.registerEntity(staticNpc2);
    }

}
