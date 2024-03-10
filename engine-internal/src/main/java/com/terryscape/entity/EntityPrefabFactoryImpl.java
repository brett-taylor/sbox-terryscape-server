package com.terryscape.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.cache.npc.NpcDefinitionNpcAppearanceType;
import com.terryscape.game.appearance.HumanoidGender;
import com.terryscape.game.chat.PlayerChatComponentImpl;
import com.terryscape.game.chat.command.CommandManager;
import com.terryscape.game.combat.CombatComponentImpl;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.combat.script.PlayerCombatScript;
import com.terryscape.game.combat.script.SimpleNpcCombatScript;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.movement.AnimationComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.game.movement.MovementSpeed;
import com.terryscape.game.npc.*;
import com.terryscape.game.player.PlayerBonusesProviderComponentImpl;
import com.terryscape.game.player.PlayerComponentImpl;
import com.terryscape.game.player.PlayerSkillsComponentImpl;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.specialattack.SpecialAttackDispatcher;
import com.terryscape.game.task.TaskComponentImpl;
import com.terryscape.maths.RandomUtil;
import com.terryscape.net.PacketManager;
import com.terryscape.world.WorldClock;
import com.terryscape.world.WorldManager;
import com.terryscape.world.pathfinding.PathfindingManager;

@Singleton
public class EntityPrefabFactoryImpl implements EntityPrefabFactory {

    private final WorldManager worldManager;

    private final PathfindingManager pathfindingManager;

    private final WorldClock worldClock;

    private final PacketManager packetManager;

    private final CommandManager commandManager;

    private final CacheLoader cacheLoader;

    private final InterfaceManager interfaceManager;

    private final CombatDiceRoll combatDiceRoll;

    private final SpecialAttackDispatcher specialAttackDispatcher;

    private final SoundManager soundManager;

    @Inject
    public EntityPrefabFactoryImpl(WorldManager worldManager,
                                   PathfindingManager pathfindingManager,
                                   WorldClock worldClock,
                                   PacketManager packetManager,
                                   CommandManager commandManager,
                                   CacheLoader cacheLoader,
                                   InterfaceManager interfaceManager,
                                   CombatDiceRoll combatDiceRoll,
                                   SpecialAttackDispatcher specialAttackDispatcher,
                                   SoundManager soundManager) {

        this.worldManager = worldManager;
        this.pathfindingManager = pathfindingManager;
        this.worldClock = worldClock;
        this.packetManager = packetManager;
        this.commandManager = commandManager;
        this.cacheLoader = cacheLoader;
        this.interfaceManager = interfaceManager;
        this.combatDiceRoll = combatDiceRoll;
        this.specialAttackDispatcher = specialAttackDispatcher;
        this.soundManager = soundManager;
    }

    @Override
    public Entity createNpcPrefab(NpcDefinition npcDefinition) {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.NPC, npcDefinition.getId());

        var npcComponent = new NpcComponentImpl(entity, worldManager);
        npcComponent.setNpcDefinition(npcDefinition);
        entity.addComponent(npcComponent);

        var npcCombatBonusesProviderComponent = new NpcCombatBonusesProviderComponent(entity, npcComponent);
        entity.addComponent(npcCombatBonusesProviderComponent);

        var npcCombatSkillsProviderComponent = new NpcCombatSkillsProviderComponent(entity, npcComponent);
        entity.addComponent(npcCombatSkillsProviderComponent);

        if (npcDefinition.getAppearanceType() == NpcDefinitionNpcAppearanceType.SIMPLE) {
            var variants = npcDefinition.getSimpleNpc().orElseThrow().getVariants();
            var randomVariant = RandomUtil.randomCollection(variants);

            var simpleNpcAppearanceComponent = new SimpleNpcAppearanceComponent(entity);
            simpleNpcAppearanceComponent.setVariant(randomVariant);
            entity.addComponent(simpleNpcAppearanceComponent);
        }

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        var movementComponent = new MovementComponentImpl(entity, pathfindingManager);
        movementComponent.setMovementSpeed(MovementSpeed.WALK);
        entity.addComponent(movementComponent);

        var health = npcDefinition.getStatsDefinition().getHealth();
        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(health);
        healthComponent.setHealth(health);
        entity.addComponent(healthComponent);

        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var combatScript = new SimpleNpcCombatScript(npcComponent);
        var combatComponent = new CombatComponentImpl(entity, pathfindingManager, cacheLoader, combatScript, combatDiceRoll);
        entity.addComponent(combatComponent);

        var overheadText = new NpcOverheadTextComponentImpl(entity, packetManager);
        entity.addComponent(overheadText);

         var npcCombatAggressionComponent = new NpcCombatAggressionComponent(entity, combatComponent, movementComponent);
         entity.addComponent(npcCombatAggressionComponent);

        return entity;
    }

    @Override
    public Entity createPlayerPrefab() {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.PLAYER, null);

        var playerComponent = new PlayerComponentImpl(entity, packetManager, interfaceManager, soundManager, cacheLoader);
        playerComponent.setGender(HumanoidGender.MALE);
        entity.addComponent(playerComponent);

        var playerBonusesProviderComponent = new PlayerBonusesProviderComponentImpl(entity, playerComponent);
        entity.addComponent(playerBonusesProviderComponent);

        var playerSkills = new PlayerSkillsComponentImpl(entity);
        entity.addComponent(playerSkills);

        var playerChatComponent = new PlayerChatComponentImpl(entity, packetManager, commandManager);
        entity.addComponent(playerChatComponent);

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        var movementComponent = new MovementComponentImpl(entity, pathfindingManager);
        movementComponent.setMovementSpeed(MovementSpeed.WALK);
        entity.addComponent(movementComponent);

        var health = playerSkills.getConstitution() * 10;
        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(health);
        healthComponent.setHealth(health);
        entity.addComponent(healthComponent);

        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var combatScript = new PlayerCombatScript(worldClock, playerComponent, specialAttackDispatcher);
        var combatComponent = new CombatComponentImpl(entity, pathfindingManager, cacheLoader, combatScript, combatDiceRoll);
        entity.addComponent(combatComponent);

        playerComponent.getInventory().addItem(cacheLoader.getItemDefinition("gold_coin"), 2000);
        playerComponent.getInventory().addItem(cacheLoader.getItemDefinition("food_fish"), 1);

        return entity;
    }
}
