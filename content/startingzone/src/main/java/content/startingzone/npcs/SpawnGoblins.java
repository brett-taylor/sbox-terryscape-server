package content.startingzone.npcs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.event.type.OnTickSystemEvent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.OnAttackedEntityEvent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcOverheadTextComponent;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldClock;
import com.terryscape.world.WorldManager;
import com.terryscape.world.coordinate.WorldCoordinate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class SpawnGoblins {

    private static final WorldCoordinate MIN_WANDER_ZONE = new WorldCoordinate(3, -16);

    private static final WorldCoordinate MAX_WANDER_ZONE = new WorldCoordinate(26, 10);

    private static final int RESPAWN_TICKS = 30;

    private final WorldManager worldManager;

    private final EntityPrefabFactory entityPrefabFactory;

    private final CacheLoader cacheLoader;

    private final WorldClock worldClock;

    private final List<NpcComponent> nonChiefAliveGoblins = new ArrayList<>();

    private List<Pair<NpcDefinition, Long>> upcomingRespawns = new ArrayList<>();

    @Inject
    public SpawnGoblins(WorldManager worldManager,
                        EntityPrefabFactory entityPrefabFactory,
                        CacheLoader cacheLoader,
                        EventSystem eventSystem, WorldClock worldClock) {

        this.worldManager = worldManager;
        this.entityPrefabFactory = entityPrefabFactory;
        this.cacheLoader = cacheLoader;
        this.worldClock = worldClock;

        eventSystem.subscribe(OnGameStartedSystemEvent.class, this::onGameStartedEvent);
        eventSystem.subscribe(OnTickSystemEvent.class, this::onTick);
    }

    private void onGameStartedEvent(OnGameStartedSystemEvent event) {
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();

        spawnGoblinWarrior();
        spawnGoblinWarrior();
        spawnGoblinWarrior();
        spawnGoblinWarrior();

        spawnGoblinShaman();
        spawnGoblinChief();

        var staticNpc1 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
        var staticNpc1Movement = staticNpc1.getComponentOrThrow(MovementComponent.class);
        staticNpc1Movement.teleport(new WorldCoordinate(14, 5));
        staticNpc1Movement.look(Direction.EAST);
        worldManager.registerEntity(staticNpc1);

        var staticNpc2 = entityPrefabFactory.createNpcPrefab(cacheLoader.getNpc("goblin_warrior"));
        var staticNpc2Movement = staticNpc2.getComponentOrThrow(MovementComponent.class);
        staticNpc2Movement.teleport(new WorldCoordinate(15, 5));
        staticNpc2Movement.look(Direction.WEST);
        worldManager.registerEntity(staticNpc2);
    }

    private void onTick(OnTickSystemEvent event) {
        var respawnsNow = upcomingRespawns.stream()
            .filter(pair -> pair.getRight() < worldClock.getNowTick())
            .toList();

        upcomingRespawns = upcomingRespawns.stream()
            .filter(pair -> pair.getRight() >= worldClock.getNowTick())
            .collect(Collectors.toList());

        for (var pair : respawnsNow) {
            switch (pair.getKey().getId()) {
                case "goblin" -> spawnRegularGoblin();
                case "goblin_warrior" -> spawnGoblinWarrior();
                case "goblin_shaman" -> spawnGoblinShaman();
                case "goblin_chief" -> spawnGoblinChief();
            }
        }
    }

    // TODO: This whole system is just written for this content module. A proper respawn system should be made.
    private void registerNpcRespawn(NpcComponent npcComponent) {
        var respawnTick = worldClock.getNowTick() + RESPAWN_TICKS;
        upcomingRespawns.add(new ImmutablePair<>(npcComponent.getNpcDefinition(), respawnTick));

        nonChiefAliveGoblins.remove(npcComponent);
    }

    private void handleChiefAttacked(OnAttackedEntityEvent event) {
        nonChiefAliveGoblins.stream()
            .filter(ignored -> RandomUtil.randomNumber(0, 10) == 0)
            .forEach(npcComponent -> {
                npcComponent.getEntity().getComponentOrThrow(NpcOverheadTextComponent.class).say("you dare touch the chief human!");
                npcComponent.getEntity().getComponentOrThrow(CombatComponent.class).attack(event.getAttacker().getEntity().getComponentOrThrow(CombatComponent.class));
            });
    }

    private void handleChiefDeath() {
        nonChiefAliveGoblins.stream()
            .filter(ignored -> RandomUtil.randomNumber(0, 3) == 0)
            .forEach(npcComponent -> npcComponent.getEntity().getComponentOrThrow(NpcOverheadTextComponent.class).say("the chief is dead!"));
    }

    private void spawnRegularGoblin() {
        var goblin = cacheLoader.getNpc("goblin");
        var npc = entityPrefabFactory.createNpcPrefab(goblin);

        npc.addComponent(new WanderMovementComponent(npc, MIN_WANDER_ZONE, MAX_WANDER_ZONE, true, cacheLoader));
        npc.addComponent(new RecurringNpcOverheadTextComponent(npc, worldClock, 180, 480, "blurgh, human"));

        npc.subscribe(OnEntityDeathEntityEvent.class, ignored -> registerNpcRespawn(npc.getComponentOrThrow(NpcComponent.class)));

        worldManager.registerEntity(npc);

        nonChiefAliveGoblins.add(npc.getComponentOrThrow(NpcComponent.class));
    }

    private void spawnGoblinWarrior() {
        var goblinWarrior = cacheLoader.getNpc("goblin_warrior");
        var npc = entityPrefabFactory.createNpcPrefab(goblinWarrior);

        npc.addComponent(new WanderMovementComponent(npc, MIN_WANDER_ZONE, MAX_WANDER_ZONE, true, cacheLoader));
        npc.addComponent(new RecurringNpcOverheadTextComponent(npc, worldClock, 180, 480, "blurgh, human"));

        npc.subscribe(OnEntityDeathEntityEvent.class, ignored -> registerNpcRespawn(npc.getComponentOrThrow(NpcComponent.class)));

        worldManager.registerEntity(npc);

        nonChiefAliveGoblins.add(npc.getComponentOrThrow(NpcComponent.class));
    }

    private void spawnGoblinShaman() {
        var goblinShaman = cacheLoader.getNpc("goblin_shaman");
        var npc = entityPrefabFactory.createNpcPrefab(goblinShaman);

        npc.addComponent(new WanderMovementComponent(npc, MIN_WANDER_ZONE, MAX_WANDER_ZONE, true, cacheLoader));
        npc.addComponent(new RecurringNpcOverheadTextComponent(npc, worldClock, 180, 360, "shaman no like human"));

        npc.subscribe(OnEntityDeathEntityEvent.class, ignored -> registerNpcRespawn(npc.getComponentOrThrow(NpcComponent.class)));

        worldManager.registerEntity(npc);

        nonChiefAliveGoblins.add(npc.getComponentOrThrow(NpcComponent.class));
    }

    private void spawnGoblinChief() {
        var goblinChief = cacheLoader.getNpc("goblin_chief");
        var npc = entityPrefabFactory.createNpcPrefab(goblinChief);

        npc.addComponent(new WanderMovementComponent(npc, MIN_WANDER_ZONE, MAX_WANDER_ZONE, true, cacheLoader));
        npc.addComponent(new RecurringNpcOverheadTextComponent(npc, worldClock, 180, 360, "human not scary"));

        npc.subscribe(OnEntityDeathEntityEvent.class, ignored -> {
            handleChiefDeath();
            registerNpcRespawn(npc.getComponentOrThrow(NpcComponent.class));
        });

        npc.subscribe(OnAttackedEntityEvent.class, this::handleChiefAttacked);

        worldManager.registerEntity(npc);
    }
}
