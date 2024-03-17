package content.startingzone.npcs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.Config;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.npc.NpcDefinition;
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

    private final NpcDefinition guiceNpcDefinition;

    private final NpcDefinition armourShopKeeperNpcDefinition;

    private final NpcDefinition weaponShopKeeperNpcDefinition;

    private final NpcDefinition foodShopKeeperNpcDefinition;

    private final NpcDefinition generalStoreShopKeeperNpcDefinition;

    private final NpcDefinition playersOnlineNpcDefinition;

    private final NpcDefinition combatExpertNpcDefinition;

    private final NpcDefinition maisieNpcDefinition;

    private final NpcDefinition tinaNpcDefinition;

    private final NpcDefinition petersNpcDefinition;

    @Inject
    public SpawnHumans(WorldManager worldManager,
                       EntityPrefabFactory entityPrefabFactory,
                       CacheLoader cacheLoader,
                       EventSystem eventSystem,
                       WorldClock worldClock,
                       @Named("guide") NpcDefinition guiceNpcDefinition,
                       @Named("armour_shop_keeper") NpcDefinition armourShopKeeperNpcDefinition,
                       @Named("weapon_shop_keeper") NpcDefinition weaponShopKeeperNpcDefinition,
                       @Named("food_shop_keeper") NpcDefinition foodShopKeeperNpcDefinition,
                       @Named("general_store_shop_keeper") NpcDefinition generalStoreShopKeeperNpcDefinition,
                       @Named("players_online_counter") NpcDefinition playersOnlineNpcDefinition,
                       @Named("combat_expert") NpcDefinition combatExpertNpcDefinition,
                       @Named("maisie") NpcDefinition maisieNpcDefinition,
                       @Named("tina") NpcDefinition tinaNpcDefinition,
                       @Named("peters") NpcDefinition petersNpcDefinition) {

        this.worldManager = worldManager;
        this.entityPrefabFactory = entityPrefabFactory;
        this.cacheLoader = cacheLoader;
        this.worldClock = worldClock;
        this.guiceNpcDefinition = guiceNpcDefinition;
        this.armourShopKeeperNpcDefinition = armourShopKeeperNpcDefinition;
        this.weaponShopKeeperNpcDefinition = weaponShopKeeperNpcDefinition;
        this.foodShopKeeperNpcDefinition = foodShopKeeperNpcDefinition;
        this.generalStoreShopKeeperNpcDefinition = generalStoreShopKeeperNpcDefinition;
        this.playersOnlineNpcDefinition = playersOnlineNpcDefinition;
        this.combatExpertNpcDefinition = combatExpertNpcDefinition;
        this.maisieNpcDefinition = maisieNpcDefinition;
        this.tinaNpcDefinition = tinaNpcDefinition;
        this.petersNpcDefinition = petersNpcDefinition;

        eventSystem.subscribe(OnGameStartedSystemEvent.class, this::onGameStartedEvent);
    }

    private void onGameStartedEvent(OnGameStartedSystemEvent event) {
        spawnGuide();

        spawnArmourShopKeeper();

        spawnWeaponShopKeeper();

        spawnFoodShopKeeper();

        spawnPlayersOnlineCounter();

        spawnCombatExpert();

        spawnTina();

        spawnMaisie();

        spawnPeters();

        spawnGeneralStoreShopKeeper();
    }

    private void spawnGuide() {
        var guide = entityPrefabFactory.createNpcPrefab(guiceNpcDefinition);
        guide.addComponent(new WanderMovementComponent(guide, new WorldCoordinate(2, 19), new WorldCoordinate(21, 31), false, cacheLoader));
        guide.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(17, 20));

        Supplier<String> overheadText = () -> "Welcome to %s! Speak to me if you need help.".formatted(Config.NAME);
        guide.addComponent(new RecurringNpcOverheadTextComponent(guide, worldClock, 60, 80, overheadText));

        worldManager.registerEntity(guide);
    }

    private void spawnArmourShopKeeper() {
        var armourShopKeeper = entityPrefabFactory.createNpcPrefab(armourShopKeeperNpcDefinition);
        armourShopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(11, 16));
        armourShopKeeper.getComponentOrThrow(MovementComponent.class).look(Direction.SOUTH);
        worldManager.registerEntity(armourShopKeeper);
    }

    private void spawnWeaponShopKeeper() {
        var weaponShopKeeper = entityPrefabFactory.createNpcPrefab(weaponShopKeeperNpcDefinition);
        weaponShopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(17, 16));
        weaponShopKeeper.getComponentOrThrow(MovementComponent.class).look(Direction.SOUTH);
        worldManager.registerEntity(weaponShopKeeper);
    }

    private void spawnFoodShopKeeper() {
        var foodShopKeeper = entityPrefabFactory.createNpcPrefab(foodShopKeeperNpcDefinition);
        foodShopKeeper.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(19, 14));
        foodShopKeeper.getComponentOrThrow(MovementComponent.class).look(Direction.WEST);
        worldManager.registerEntity(foodShopKeeper);
    }

    private void spawnPlayersOnlineCounter() {
        var playersOnlineCounter = entityPrefabFactory.createNpcPrefab(playersOnlineNpcDefinition);
        playersOnlineCounter.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(1, 24));
        playersOnlineCounter.getComponentOrThrow(MovementComponent.class).look(Direction.EAST);

        Supplier<String> overheadText = () -> "There are currently %s players online.".formatted(worldManager.getPlayers().size());
        playersOnlineCounter.addComponent(new RecurringNpcOverheadTextComponent(playersOnlineCounter, worldClock, 60, 80, overheadText));

        worldManager.registerEntity(playersOnlineCounter);
    }

    private void spawnCombatExpert() {
        var npc = entityPrefabFactory.createNpcPrefab(combatExpertNpcDefinition);
        npc.addComponent(new WanderMovementComponent(npc, new WorldCoordinate(8, 28), new WorldCoordinate(12, 33), false, cacheLoader));
        npc.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(9, 30));

        worldManager.registerEntity(npc);
    }

    private void spawnMaisie() {
        var npc = entityPrefabFactory.createNpcPrefab(maisieNpcDefinition);
        npc.addComponent(new WanderMovementComponent(npc, new WorldCoordinate(-2, 29), new WorldCoordinate(0, 33), false, cacheLoader));
        npc.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(-2, 29));

        worldManager.registerEntity(npc);
    }

    private void spawnPeters() {
        var npc = entityPrefabFactory.createNpcPrefab(petersNpcDefinition);
        npc.addComponent(new WanderMovementComponent(npc, new WorldCoordinate(2, 34), new WorldCoordinate(4, 37), false, cacheLoader));
        npc.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(2, 34));

        worldManager.registerEntity(npc);
    }

    private void spawnTina() {
        var npc = entityPrefabFactory.createNpcPrefab(tinaNpcDefinition);
        npc.addComponent(new WanderMovementComponent(npc, new WorldCoordinate(6, 34), new WorldCoordinate(10, 38), false, cacheLoader));
        npc.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(6, 34));

        worldManager.registerEntity(npc);
    }

    private void spawnGeneralStoreShopKeeper() {
        var npc = entityPrefabFactory.createNpcPrefab(generalStoreShopKeeperNpcDefinition);
        npc.getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(13,16 ));
        npc.getComponentOrThrow(MovementComponent.class).look(Direction.SOUTH);

        worldManager.registerEntity(npc);
    }
}
