package content.devtools.testnps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.world.WorldCoordinate;
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
        var spawnCoordinate = new WorldCoordinate(10, 10);
        var wanderRadius = 19;

        for (int i = 0; i < 2; i++) {
            var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin"));
            var movement = npc.getComponentOrThrow(MovementComponent.class);
            movement.teleport(spawnCoordinate);
            npc.addComponent(new WanderMovementComponent(npc, wanderRadius));
            worldManager.registerEntity(npc, worldManager.getWorldRegionFromWorldCoordinate(movement.getWorldCoordinate()));
        }

        for (int i = 0; i < 2; i++) {
            var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
            var movement = npc.getComponentOrThrow(MovementComponent.class);
            movement.teleport(spawnCoordinate);
            npc.addComponent(new WanderMovementComponent(npc, wanderRadius));
            worldManager.registerEntity(npc, worldManager.getWorldRegionFromWorldCoordinate(movement.getWorldCoordinate()));
        }

        var npc1 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_shaman"));
        var npc1Movement = npc1.getComponentOrThrow(MovementComponent.class);
        npc1Movement.teleport(spawnCoordinate);
        npc1.addComponent(new WanderMovementComponent(npc1, wanderRadius));
        worldManager.registerEntity(npc1, worldManager.getWorldRegionFromWorldCoordinate(npc1Movement.getWorldCoordinate()));

        var npc2 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_chief"));
        var npc2Movement = npc2.getComponentOrThrow(MovementComponent.class);
        npc2Movement.teleport(spawnCoordinate);
        npc2.addComponent(new WanderMovementComponent(npc2, wanderRadius));
        worldManager.registerEntity(npc2, worldManager.getWorldRegionFromWorldCoordinate(npc2Movement.getWorldCoordinate()));

        var staticNpc1 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
        var staticNpc1Movement = staticNpc1.getComponentOrThrow(MovementComponent.class);
        staticNpc1Movement.teleport(new WorldCoordinate(6, 7));
        worldManager.registerEntity(staticNpc1, worldManager.getWorldRegionFromWorldCoordinate(staticNpc1Movement.getWorldCoordinate()));

        var staticNpc2 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
        var staticNpc2Movement = staticNpc2.getComponentOrThrow(MovementComponent.class);
        staticNpc2Movement.teleport(new WorldCoordinate(7, 6));
        worldManager.registerEntity(staticNpc2, worldManager.getWorldRegionFromWorldCoordinate(staticNpc2Movement.getWorldCoordinate()));
    }

}
