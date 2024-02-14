package content.devtools.testnps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.world.coordinate.WorldCoordinate;
import com.terryscape.world.WorldManager;

@Singleton
public class SpawnSomeTestNpcs {

    private final WorldManager worldManager;

    private final EntityPrefabFactory entityPrefabFactory;

    private final CacheLoader cacheLoader;

    @Inject
    public SpawnSomeTestNpcs(WorldManager worldManager, EntityPrefabFactory entityPrefabFactory, CacheLoader cacheLoader, EventSystem eventSystem) {
        this.worldManager = worldManager;
        this.entityPrefabFactory = entityPrefabFactory;
        this.cacheLoader = cacheLoader;

        eventSystem.subscribe(OnGameStartedSystemEvent.class, this::onGameStartedEvent);
    }

    public void onGameStartedEvent(OnGameStartedSystemEvent event) {
        var spawnCoordinate = new WorldCoordinate(15, 15);
        var wanderRadius = 10;

        for (int i = 0; i < 4; i++) {
            var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin"));
            var movement = npc.getComponentOrThrow(MovementComponent.class);
            movement.teleport(spawnCoordinate);
            npc.addComponent(new WanderMovementComponent(npc, wanderRadius));
            worldManager.registerEntity(npc);
        }

        for (int i = 0; i < 4; i++) {
            var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
            var movement = npc.getComponentOrThrow(MovementComponent.class);
            movement.teleport(spawnCoordinate);
            npc.addComponent(new WanderMovementComponent(npc, wanderRadius));
            worldManager.registerEntity(npc);
        }

        var npc1 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_shaman"));
        var npc1Movement = npc1.getComponentOrThrow(MovementComponent.class);
        npc1Movement.teleport(spawnCoordinate);
        npc1.addComponent(new WanderMovementComponent(npc1, wanderRadius));
        worldManager.registerEntity(npc1);

        var npc2 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_chief"));
        var npc2Movement = npc2.getComponentOrThrow(MovementComponent.class);
        npc2Movement.teleport(spawnCoordinate);
        npc2.addComponent(new WanderMovementComponent(npc2, wanderRadius));
        worldManager.registerEntity(npc2);

        var staticNpc1 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
        var staticNpc1Movement = staticNpc1.getComponentOrThrow(MovementComponent.class);
        staticNpc1Movement.teleport(new WorldCoordinate(7, 7));
        worldManager.registerEntity(staticNpc1);

        var staticNpc2 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
        var staticNpc2Movement = staticNpc2.getComponentOrThrow(MovementComponent.class);
        staticNpc2Movement.teleport(new WorldCoordinate(8, 6));
        worldManager.registerEntity(staticNpc2);
    }

}
