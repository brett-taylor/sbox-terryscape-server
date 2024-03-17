package content.startingzone.npcs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.entity.event.type.OnDeathEntityEvent;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.event.type.OnTickSystemEvent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.OnAttackedEntityEvent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.game.npc.NpcOverheadTextComponent;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.WorldClock;
import com.terryscape.entity.EntityManager;
import com.terryscape.world.coordinate.WorldCoordinate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Singleton
public class SpawnGoblins {

    private static final WorldCoordinate MIN_WANDER_ZONE = new WorldCoordinate(3, -15);

    private static final WorldCoordinate MAX_WANDER_ZONE = new WorldCoordinate(45, 4);

    private static final int RESPAWN_TICKS = 30;

    private final EntityManager entityManager;

    private final EntityPrefabFactory entityPrefabFactory;

    private final WorldClock worldClock;

    private final CacheLoader cacheLoader;

    private final NpcDefinition goblinNpcDefinition;

    private final NpcDefinition goblinWarriorNpcDefinition;

    private final NpcDefinition goblinShamanNpcDefinition;

    private final NpcDefinition goblinChiefNpcDefinition;

    private final List<NpcComponent> nonChiefAliveGoblins = new ArrayList<>();

    private List<Pair<NpcDefinition, Long>> upcomingRespawns = new ArrayList<>();

    private NpcComponent goblinShaman;

    @Inject
    public SpawnGoblins(EntityManager entityManager,
                        EntityPrefabFactory entityPrefabFactory,
                        EventSystem eventSystem,
                        WorldClock worldClock,
                        CacheLoader cacheLoader,
                        @Named("goblin") NpcDefinition goblinNpcDefinition,
                        @Named("goblin_warrior") NpcDefinition goblinWarriorNpcDefinition,
                        @Named("goblin_shaman") NpcDefinition goblinShamanNpcDefinition,
                        @Named("goblin_chief") NpcDefinition goblinChiefNpcDefinition) {

        this.entityManager = entityManager;
        this.entityPrefabFactory = entityPrefabFactory;
        this.worldClock = worldClock;
        this.cacheLoader = cacheLoader;
        this.goblinNpcDefinition = goblinNpcDefinition;
        this.goblinWarriorNpcDefinition = goblinWarriorNpcDefinition;
        this.goblinShamanNpcDefinition = goblinShamanNpcDefinition;
        this.goblinChiefNpcDefinition = goblinChiefNpcDefinition;

        eventSystem.subscribe(OnGameStartedSystemEvent.class, this::onGameStartedEvent);
        eventSystem.subscribe(OnTickSystemEvent.class, this::onTick);
    }

    private void onGameStartedEvent(OnGameStartedSystemEvent event) {
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();
        spawnRegularGoblin();

        spawnGoblinWarrior();
        spawnGoblinWarrior();
        spawnGoblinWarrior();
        spawnGoblinWarrior();
        spawnGoblinWarrior();
        spawnGoblinWarrior();
        spawnGoblinWarrior();
        spawnGoblinWarrior();

        spawnGoblinShaman();
        spawnGoblinShaman();

        spawnGoblinChief();
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
            .filter(npc -> npc != goblinShaman)
            .filter(npc -> !goblinShaman.getEntity().getComponentOrThrow(CombatComponent.class).isInCombat())
            .forEach(npcComponent -> {
                npcComponent.getEntity().getComponentOrThrow(NpcOverheadTextComponent.class).say("you dare touch the chief human");
                npcComponent.getEntity().getComponentOrThrow(CombatComponent.class).attack(event.getAttacker().getEntity().getComponentOrThrow(CombatComponent.class));
            });

        if (goblinShaman == null) {
            return;
        }

        var isGoblinShamanAttacking = goblinShaman.getEntity().getComponentOrThrow(CombatComponent.class).isInCombat();
        if (isGoblinShamanAttacking) {
            return;
        }

        goblinShaman.getEntity().getComponentOrThrow(NpcOverheadTextComponent.class).say("protect the chief");
        goblinShaman.getEntity().getComponentOrThrow(CombatComponent.class).attack(event.getAttacker().getEntity().getComponentOrThrow(CombatComponent.class));
    }

    private void handleChiefDeath() {
        if (nonChiefAliveGoblins.isEmpty()) {
            return;
        }

        var randomGoblin = RandomUtil.randomCollection(nonChiefAliveGoblins);
        randomGoblin.getEntity().getComponentOrThrow(NpcOverheadTextComponent.class).say("the chief is dead");
    }

    private void spawnRegularGoblin() {
        var npc = entityPrefabFactory.createNpcPrefab(goblinNpcDefinition);

        npc.addComponent(new WanderMovementComponent(npc, MIN_WANDER_ZONE, MAX_WANDER_ZONE, true, cacheLoader));

        Supplier<String> overheadText = () -> "blurgh humans";
        npc.addComponent(new RecurringNpcOverheadTextComponent(npc, worldClock, 180, 480, overheadText));

        npc.subscribe(OnDeathEntityEvent.class, ignored -> registerNpcRespawn(npc.getComponentOrThrow(NpcComponent.class)));

        entityManager.registerEntity(npc);

        nonChiefAliveGoblins.add(npc.getComponentOrThrow(NpcComponent.class));
    }

    private void spawnGoblinWarrior() {
        var npc = entityPrefabFactory.createNpcPrefab(goblinWarriorNpcDefinition);

        npc.addComponent(new WanderMovementComponent(npc, MIN_WANDER_ZONE, MAX_WANDER_ZONE, true, cacheLoader));

        Supplier<String> overheadText = () -> "blurgh humans";
        npc.addComponent(new RecurringNpcOverheadTextComponent(npc, worldClock, 180, 480, overheadText));

        npc.subscribe(OnDeathEntityEvent.class, ignored -> registerNpcRespawn(npc.getComponentOrThrow(NpcComponent.class)));

        entityManager.registerEntity(npc);

        nonChiefAliveGoblins.add(npc.getComponentOrThrow(NpcComponent.class));
    }

    private void spawnGoblinShaman() {
        var npc = entityPrefabFactory.createNpcPrefab(goblinShamanNpcDefinition);

        npc.addComponent(new WanderMovementComponent(npc, MIN_WANDER_ZONE, MAX_WANDER_ZONE, true, cacheLoader));

        Supplier<String> overheadText = () -> "shaman no like human";
        npc.addComponent(new RecurringNpcOverheadTextComponent(npc, worldClock, 180, 360, overheadText));

        npc.getComponentOrThrow(CombatComponent.class).setCombatScript(new GoblinShamanCombatScript());

        npc.subscribe(OnDeathEntityEvent.class, ignored -> registerNpcRespawn(npc.getComponentOrThrow(NpcComponent.class)));

        entityManager.registerEntity(npc);
        nonChiefAliveGoblins.add(npc.getComponentOrThrow(NpcComponent.class));
        this.goblinShaman = npc.getComponentOrThrow(NpcComponent.class);
    }

    private void spawnGoblinChief() {
        var npc = entityPrefabFactory.createNpcPrefab(goblinChiefNpcDefinition);

        npc.addComponent(new WanderMovementComponent(npc, MIN_WANDER_ZONE, MAX_WANDER_ZONE, true, cacheLoader));

        Supplier<String> overheadText = () -> "human not scary";
        npc.addComponent(new RecurringNpcOverheadTextComponent(npc, worldClock, 180, 360, overheadText));

        npc.subscribe(OnDeathEntityEvent.class, ignored -> {
            handleChiefDeath();
            registerNpcRespawn(npc.getComponentOrThrow(NpcComponent.class));
        });

        npc.subscribe(OnAttackedEntityEvent.class, this::handleChiefAttacked);

        entityManager.registerEntity(npc);
    }
}
