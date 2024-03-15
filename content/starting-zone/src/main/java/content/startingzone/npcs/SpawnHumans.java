package content.startingzone.npcs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldClock;
import com.terryscape.world.WorldManager;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.util.function.Supplier;

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

    private void onGameStartedEvent(OnGameStartedSystemEvent event) {
        spawnGuide();

        spawnArmourShopKeeper();

        spawnWeaponShopKeeper();

        spawnFoodShopKeeper();

        spawnPlayersOnlineCounter();

        spawnCombatGuide();
    }

    private void spawnGuide() {
        var guide = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("guide"));
        guide.addComponent(new WanderMovementComponent(guide, new WorldCoordinate(2, 19), new WorldCoordinate(21, 31), false, cacheLoader));
        guide.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(17, 20));

        Supplier<String> overheadText = () -> "Welcome to %s! Speak to me if you need help.".formatted(Config.NAME);
        guide.addComponent(new RecurringNpcOverheadTextComponent(guide, worldClock, 60, 80, overheadText));

        worldManager.registerEntity(guide);
    }

    private void spawnArmourShopKeeper() {
        var armourShopKeeper = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("armour_shop_keeper"));
        armourShopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(11, 16));
        armourShopKeeper.getComponentOrThrow(MovementComponent.class).look(Direction.SOUTH);
        worldManager.registerEntity(armourShopKeeper);
    }

    private void spawnWeaponShopKeeper() {
        var weaponShopKeeper = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("weapon_shop_keeper"));
        weaponShopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(17, 16));
        weaponShopKeeper.getComponentOrThrow(MovementComponent.class).look(Direction.SOUTH);
        worldManager.registerEntity(weaponShopKeeper);
    }

    private void spawnFoodShopKeeper() {
        var foodShopKeeper = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("food_shop_keeper"));
        foodShopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(19, 14));
        foodShopKeeper.getComponentOrThrow(MovementComponent.class).look(Direction.WEST);
        worldManager.registerEntity(foodShopKeeper);
    }

    private void spawnPlayersOnlineCounter() {
        var playersOnlineCounter = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("players_online_counter"));
        playersOnlineCounter.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(1, 24));
        playersOnlineCounter.getComponentOrThrow(MovementComponent.class).look(Direction.EAST);

        Supplier<String> overheadText = () -> "There are currently %s players online.".formatted(worldManager.getPlayers().size());
        playersOnlineCounter.addComponent(new RecurringNpcOverheadTextComponent(playersOnlineCounter, worldClock, 60, 80, overheadText));

        worldManager.registerEntity(playersOnlineCounter);
    }

    private void spawnCombatGuide() {
        var combatGuide = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("combat_guide"));
        combatGuide.addComponent(new WanderMovementComponent(combatGuide, new WorldCoordinate(8, 28), new WorldCoordinate(12, 33), false, cacheLoader));
        combatGuide.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(9, 30));

        worldManager.registerEntity(combatGuide);
    }

    private void spawnMaisie() {
        var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("maisie"));
        npc.addComponent(new WanderMovementComponent(npc, new WorldCoordinate(-2, 29), new WorldCoordinate(0, 33), false, cacheLoader));
        npc.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(-2, 29));

        worldManager.registerEntity(npc);
    }

    private void spawnPeters() {
        var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("peters"));
        npc.addComponent(new WanderMovementComponent(npc, new WorldCoordinate(2, 34), new WorldCoordinate(4, 37), false, cacheLoader));
        npc.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(2, 34));

        worldManager.registerEntity(npc);
    }

    private void spawnTina() {
        var npc = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpcDefinition("tina"));
        npc.addComponent(new WanderMovementComponent(npc, new WorldCoordinate(6, 34), new WorldCoordinate(10, 38), false, cacheLoader));
        npc.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(6, 34));

        worldManager.registerEntity(npc);
    }
}
