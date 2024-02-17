package content.startingzone.spawnnpcs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.world.WorldManager;
import com.terryscape.world.coordinate.WorldCoordinate;

@Singleton
public class SpawnHumans {

    private final WorldManager worldManager;

    private final EntityPrefabFactory entityPrefabFactory;

    private final CacheLoader cacheLoader;

    @Inject
    public SpawnHumans(WorldManager worldManager, EntityPrefabFactory entityPrefabFactory, CacheLoader cacheLoader, EventSystem eventSystem) {
        this.worldManager = worldManager;
        this.entityPrefabFactory = entityPrefabFactory;
        this.cacheLoader = cacheLoader;

        eventSystem.subscribe(OnGameStartedSystemEvent.class, this::onGameStartedEvent);
    }

    public void onGameStartedEvent(OnGameStartedSystemEvent event) {
        var shopKeeper = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("shop_keeper"));
        shopKeeper.addComponent(new WanderMovementComponent(shopKeeper, new WorldCoordinate(11, 12), new WorldCoordinate(18, 17), false));
        shopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(12, 15));
        worldManager.registerEntity(shopKeeper);

        var guide = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("guide"));
        guide.addComponent(new WanderMovementComponent(guide, new WorldCoordinate(9, 19), new WorldCoordinate(19, 24), false));
        guide.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(17, 20));
        worldManager.registerEntity(guide);
    }

}
