package content.startingzone.spawnnpcs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.world.WorldClock;
import com.terryscape.world.WorldManager;
import com.terryscape.world.coordinate.WorldCoordinate;
import content.startingzone.RecurringNpcOverheadTextComponent;

@Singleton
public class SpawnHumans {

    private final WorldManager worldManager;

    private final EntityPrefabFactory entityPrefabFactory;

    private final CacheLoader cacheLoader;

    private final WorldClock worldClock;

    @Inject
    public SpawnHumans(WorldManager worldManager,
                       EntityPrefabFactory entityPrefabFactory,
                       CacheLoader cacheLoader,
                       EventSystem eventSystem,
                       WorldClock worldClock) {

        this.worldManager = worldManager;
        this.entityPrefabFactory = entityPrefabFactory;
        this.cacheLoader = cacheLoader;
        this.worldClock = worldClock;

        eventSystem.subscribe(OnGameStartedSystemEvent.class, this::onGameStartedEvent);
    }

    public void onGameStartedEvent(OnGameStartedSystemEvent event) {
        var armourShopKeeper = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("armour_shop_keeper"));
        armourShopKeeper.addComponent(new WanderMovementComponent(armourShopKeeper, new WorldCoordinate(11, 12), new WorldCoordinate(18, 17), false));
        armourShopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(12, 15));
        worldManager.registerEntity(armourShopKeeper);

        var weaponShopKeeper = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("weapon_shop_keeper"));
        weaponShopKeeper.addComponent(new WanderMovementComponent(weaponShopKeeper, new WorldCoordinate(11, 12), new WorldCoordinate(18, 17), false));
        weaponShopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(18, 14));
        worldManager.registerEntity(weaponShopKeeper);

        var guide = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("guide"));
        guide.addComponent(new WanderMovementComponent(guide, new WorldCoordinate(9, 19), new WorldCoordinate(19, 24), false));
        guide.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(17, 20));
        guide.addComponent(new RecurringNpcOverheadTextComponent(guide, worldClock, 60, 80, "Welcome to %s! Speak to me if you need help.".formatted(Config.NAME)));
        worldManager.registerEntity(guide);

    }

}
