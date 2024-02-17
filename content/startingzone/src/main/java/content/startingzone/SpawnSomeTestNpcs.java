package content.startingzone;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.world.Direction;
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
        var minWanderZone = new WorldCoordinate(3, -16);
        var maxWanderZone = new WorldCoordinate(24, 24);

        for (int i = 0; i < 4; i++) {
            var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin"));
            npc.addComponent(new WanderMovementComponent(npc, minWanderZone, maxWanderZone));
            worldManager.registerEntity(npc);
        }

        for (int i = 0; i < 4; i++) {
            var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
            npc.addComponent(new WanderMovementComponent(npc, minWanderZone, maxWanderZone));
            worldManager.registerEntity(npc);
        }

        var npc1 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_shaman"));
        npc1.addComponent(new WanderMovementComponent(npc1, minWanderZone, maxWanderZone));
        worldManager.registerEntity(npc1);

        var npc2 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_chief"));
        npc2.addComponent(new WanderMovementComponent(npc2, minWanderZone, maxWanderZone));
        worldManager.registerEntity(npc2);

        var staticNpc1 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
        var staticNpc1Movement = staticNpc1.getComponentOrThrow(MovementComponent.class);
        staticNpc1Movement.teleport(new WorldCoordinate(14, 23));
        staticNpc1Movement.look(Direction.SOUTH);
        worldManager.registerEntity(staticNpc1);

        var staticNpc2 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
        var staticNpc2Movement = staticNpc2.getComponentOrThrow(MovementComponent.class);
        staticNpc2Movement.teleport(new WorldCoordinate(15, 23));
        staticNpc2Movement.look(Direction.SOUTH);
        worldManager.registerEntity(staticNpc2);
    }

}
