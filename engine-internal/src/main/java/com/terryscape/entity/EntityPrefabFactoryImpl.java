package com.terryscape.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.cache.npc.NpcDefinitionNpcAppearanceType;
import com.terryscape.cache.projectile.ProjectileDefinition;
import com.terryscape.entity.component.ComponentSystemManager;
import com.terryscape.game.animation.AnimationComponent;
import com.terryscape.game.appearance.HumanoidGender;
import com.terryscape.game.chat.PlayerChatSystem;
import com.terryscape.game.combat.CombatComponentImpl;
import com.terryscape.game.combat.combatscript.BasicNpcCombatScript;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.grounditem.GroundItemComponent;
import com.terryscape.game.grounditem.GroundItemTimeAliveComponent;
import com.terryscape.game.item.ItemContainerItem;
import com.terryscape.game.loottable.LootTableManager;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.game.movement.MovementSpeed;
import com.terryscape.game.npc.*;
import com.terryscape.game.player.PlayerBonusesProviderComponentImpl;
import com.terryscape.game.player.PlayerCombatScript;
import com.terryscape.game.player.PlayerComponentImpl;
import com.terryscape.game.player.PlayerSkillsComponentImpl;
import com.terryscape.game.projectile.ProjectileComponent;
import com.terryscape.game.projectile.ProjectileFactory;
import com.terryscape.game.specialattack.SpecialAttackDispatcher;
import com.terryscape.game.task.TaskComponentImpl;
import com.terryscape.game.world.WorldClock;
import com.terryscape.game.world.coordinate.WorldCoordinate;
import com.terryscape.game.world.pathfinding.PathfindingManager;
import com.terryscape.maths.RandomUtil;
import com.terryscape.net.PacketManager;

// TODO: Work out a plan on how to get rid of this.

@Singleton
public class EntityPrefabFactoryImpl implements EntityPrefabFactory {

    private final PathfindingManager pathfindingManager;

    private final WorldClock worldClock;

    private final PacketManager packetManager;

    private final CombatDiceRoll combatDiceRoll;

    private final SpecialAttackDispatcher specialAttackDispatcher;

    private final LootTableManager lootTableManager;

    private final ProjectileFactory projectileFactory;

    private final ItemDefinition goldCoinItemDefinition;

    private final ItemDefinition foodFishItemDefinition;

    private final ComponentSystemManager componentSystemManager;

    private final PlayerChatSystem playerChatSystem;

    @Inject
    public EntityPrefabFactoryImpl(PathfindingManager pathfindingManager,
                                   WorldClock worldClock,
                                   PacketManager packetManager,
                                   CombatDiceRoll combatDiceRoll,
                                   SpecialAttackDispatcher specialAttackDispatcher,
                                   LootTableManager lootTableManager,
                                   ProjectileFactory projectileFactory,
                                   @Named("gold_coin") ItemDefinition goldCoinItemDefinition,
                                   @Named("food_fish") ItemDefinition foodFishItemDefinition,
                                   ComponentSystemManager componentSystemManager,
                                   PlayerChatSystem playerChatSystem) {

        this.pathfindingManager = pathfindingManager;
        this.worldClock = worldClock;
        this.packetManager = packetManager;
        this.combatDiceRoll = combatDiceRoll;
        this.specialAttackDispatcher = specialAttackDispatcher;
        this.lootTableManager = lootTableManager;
        this.projectileFactory = projectileFactory;
        this.goldCoinItemDefinition = goldCoinItemDefinition;
        this.foodFishItemDefinition = foodFishItemDefinition;
        this.componentSystemManager = componentSystemManager;
        this.playerChatSystem = playerChatSystem;
    }

    @Override
    public Entity createNpcPrefab(NpcDefinition npcDefinition) {
        var entity = new EntityImpl(componentSystemManager, EntityIdentifier.randomIdentifier(), EntityPrefabType.NPC, npcDefinition.getId());

        var npcComponent = new NpcComponentImpl(lootTableManager);
        npcComponent.setNpcDefinition(npcDefinition);
        entity.addComponent(npcComponent);

        var npcCombatBonusesProviderComponent = new NpcCombatBonusesProviderComponent(npcComponent);
        entity.addComponent(npcCombatBonusesProviderComponent);

        var npcCombatSkillsProviderComponent = new NpcCombatSkillsProviderComponent(npcComponent);
        entity.addComponent(npcCombatSkillsProviderComponent);

        if (npcDefinition.getAppearanceType() == NpcDefinitionNpcAppearanceType.SIMPLE) {
            var variants = npcDefinition.getSimpleNpc().orElseThrow().getVariants();
            var randomVariant = RandomUtil.randomCollection(variants);

            var simpleNpcAppearanceComponent = new SimpleNpcAppearanceComponent();
            simpleNpcAppearanceComponent.setVariant(randomVariant);
            entity.addComponent(simpleNpcAppearanceComponent);
        }

        var taskComponent = new TaskComponentImpl();
        entity.addComponent(taskComponent);

        var health = npcDefinition.getStatsDefinition().getHealth();
        var healthComponent = new HealthComponentImpl();
        healthComponent.setMaxHealth(health);
        healthComponent.setHealth(health);
        entity.addComponent(healthComponent);

        entity.addComponent(new AnimationComponent());

        var movementComponent = new MovementComponentImpl(pathfindingManager);
        movementComponent.setMovementSpeed(MovementSpeed.WALK);
        entity.addComponent(movementComponent);

        var combatComponent = new CombatComponentImpl(pathfindingManager, combatDiceRoll, projectileFactory, playerChatSystem);
        entity.addComponent(combatComponent);

        combatComponent.setCombatScript(new BasicNpcCombatScript());

        var overheadText = new NpcOverheadTextComponentImpl(packetManager);
        entity.addComponent(overheadText);

        var npcCombatAggressionComponent = new NpcCombatAggressionComponent(combatComponent, movementComponent);
        entity.addComponent(npcCombatAggressionComponent);

        return entity;
    }

    @Override
    public Entity createPlayerPrefab() {
        var entity = new EntityImpl(componentSystemManager, EntityIdentifier.randomIdentifier(), EntityPrefabType.PLAYER, null);

        var playerComponent = new PlayerComponentImpl();
        playerComponent.setGender(RandomUtil.randomBool() ? HumanoidGender.MALE : HumanoidGender.FEMALE);
        entity.addComponent(playerComponent);

        var playerBonusesProviderComponent = new PlayerBonusesProviderComponentImpl(playerComponent);
        entity.addComponent(playerBonusesProviderComponent);

        var playerSkills = new PlayerSkillsComponentImpl();
        entity.addComponent(playerSkills);

        var taskComponent = new TaskComponentImpl();
        entity.addComponent(taskComponent);

        var health = playerSkills.getConstitution() * 10;
        var healthComponent = new HealthComponentImpl();
        healthComponent.setMaxHealth(health);
        healthComponent.setHealth(health);
        entity.addComponent(healthComponent);

        entity.addComponent(new AnimationComponent());

        var movementComponent = new MovementComponentImpl(pathfindingManager);
        movementComponent.setMovementSpeed(MovementSpeed.WALK);
        entity.addComponent(movementComponent);

        var combatComponent = new CombatComponentImpl(pathfindingManager, combatDiceRoll, projectileFactory, playerChatSystem);
        entity.addComponent(combatComponent);

        combatComponent.setCombatScript(new PlayerCombatScript(worldClock, specialAttackDispatcher));

        playerComponent.getInventory().addItem(goldCoinItemDefinition, 250);
        playerComponent.getInventory().addItem(foodFishItemDefinition, 1);

        return entity;
    }

    @Override
    public Entity createGroundItemPrefab(ItemContainerItem itemContainerItem, WorldCoordinate worldCoordinate) {
        var entity = new EntityImpl(componentSystemManager, EntityIdentifier.randomIdentifier(), EntityPrefabType.GROUND_ITEM, null);

        var groundItemComponent = new GroundItemComponent(itemContainerItem, worldCoordinate);
        entity.addComponent(groundItemComponent);

        var groundItemTimeAliveComponent = new GroundItemTimeAliveComponent();
        entity.addComponent(groundItemTimeAliveComponent);

        return entity;
    }

    @Override
    public Entity createProjectilePrefab(ProjectileDefinition projectileDefinition) {
        var entity = new EntityImpl(componentSystemManager, EntityIdentifier.randomIdentifier(), EntityPrefabType.PROJECTILE, projectileDefinition.getId());

        var projectileComponent = new ProjectileComponent(projectileDefinition);
        entity.addComponent(projectileComponent);

        return entity;
    }
}
