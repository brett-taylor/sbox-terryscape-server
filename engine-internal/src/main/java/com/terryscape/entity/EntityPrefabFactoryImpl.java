package com.terryscape.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.cache.npc.NpcDefinitionNpcAppearanceType;
import com.terryscape.game.ParticleComponentImpl;
import com.terryscape.game.appearance.HumanoidGender;
import com.terryscape.game.chat.PlayerChatComponentImpl;
import com.terryscape.game.chat.command.CommandManager;
import com.terryscape.game.chat.dialogue.PlayerDialogueComponentImpl;
import com.terryscape.game.combat.CharacterStatsImpl;
import com.terryscape.game.combat.CombatComponentImpl;
import com.terryscape.game.ProjectileComponentImpl;
import com.terryscape.game.combat.ParticleComponent;
import com.terryscape.game.combat.SpecialBarImpl;
import com.terryscape.game.combat.health.HealthComponentImpl;
import com.terryscape.game.combat.script.PlayerCombatScript;
import com.terryscape.game.combat.script.SimpleNpcCombatScript;
import com.terryscape.game.combat.special.WeaponSpecialAttackDispatcher;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.movement.AnimationComponentImpl;
import com.terryscape.game.movement.MovementComponentImpl;
import com.terryscape.game.movement.MovementSpeed;
import com.terryscape.game.npc.NpcComponentImpl;
import com.terryscape.game.npc.SimpleNpcAppearanceComponent;
import com.terryscape.game.player.PlayerComponentImpl;
import com.terryscape.game.task.TaskComponentImpl;
import com.terryscape.maths.RandomUtil;
import com.terryscape.net.PacketManager;
import com.terryscape.world.WorldClock;
import com.terryscape.world.WorldManager;
import com.terryscape.world.coordinate.WorldCoordinate;
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

    private final WeaponSpecialAttackDispatcher weaponSpecialAttackDispatcher;

    @Inject
    public EntityPrefabFactoryImpl(WorldManager worldManager,
                                   PathfindingManager pathfindingManager,
                                   WorldClock worldClock,
                                   PacketManager packetManager,
                                   CommandManager commandManager,
                                   CacheLoader cacheLoader,
                                   InterfaceManager interfaceManager,
                                   WeaponSpecialAttackDispatcher weaponSpecialAttackDispatcher) {

        this.worldManager = worldManager;
        this.pathfindingManager = pathfindingManager;
        this.worldClock = worldClock;
        this.packetManager = packetManager;
        this.commandManager = commandManager;
        this.cacheLoader = cacheLoader;
        this.interfaceManager = interfaceManager;
        this.weaponSpecialAttackDispatcher = weaponSpecialAttackDispatcher;
    }

    @Override
    public Entity createNpcPrefab(NpcDefinition npcDefinition) {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.NPC, npcDefinition.getId());

        var npcComponent = new NpcComponentImpl(entity, worldManager);
        npcComponent.setNpcDefinition(npcDefinition);
        entity.addComponent(npcComponent);

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

        var maxHealth = 40;
        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(maxHealth);
        healthComponent.setHealth(maxHealth);
        entity.addComponent(healthComponent);

        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var combatScript = new SimpleNpcCombatScript(worldClock, npcComponent, npcDefinition.getDamageType());
        var combatComponent = new CombatComponentImpl(entity, pathfindingManager, cacheLoader, combatScript);
        entity.addComponent(combatComponent);

        var statsComponent = new CharacterStatsImpl(entity);
        entity.addComponent(statsComponent);

        statsComponent.SetStats(npcDefinition.getCombatStats());
        npcDefinition.getAttackBonuses().forEach(x -> statsComponent.AddAttackBonus(x.getLeft(), x.getRight()));
        npcDefinition.getDefenseBonuses().forEach(x -> statsComponent.AddDefenseBonus(x.getLeft(), x.getRight()));
        
        return entity;
    }

    @Override
    public Entity createPlayerPrefab() {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.PLAYER, null);

        var playerComponent = new PlayerComponentImpl(entity, packetManager, interfaceManager);
        playerComponent.setGender(HumanoidGender.MALE);
        entity.addComponent(playerComponent);

        var playerChatComponent = new PlayerChatComponentImpl(entity, packetManager, commandManager);
        entity.addComponent(playerChatComponent);

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        var movementComponent = new MovementComponentImpl(entity, pathfindingManager);
        movementComponent.setMovementSpeed(MovementSpeed.RUN);
        entity.addComponent(movementComponent);
		
        var maxHealth = 160;
        var healthComponent = new HealthComponentImpl(entity);
        healthComponent.setMaxHealth(maxHealth);
        healthComponent.setHealth(maxHealth);
        entity.addComponent(healthComponent);

        var animationComponent = new AnimationComponentImpl(entity);
        entity.addComponent(animationComponent);

        var specialComponent = new SpecialBarImpl(entity);
        entity.addComponent(specialComponent);

        var combatScript = new PlayerCombatScript(worldClock, playerComponent, weaponSpecialAttackDispatcher);
        var combatComponent = new CombatComponentImpl(entity, pathfindingManager, cacheLoader, combatScript);
        entity.addComponent(combatComponent);

        var dialogueComponent = new PlayerDialogueComponentImpl(entity, interfaceManager);
        entity.addComponent(dialogueComponent);

        var statsComponent = new CharacterStatsImpl(entity);
        entity.addComponent(statsComponent);

        return entity;
    }

    public Entity createProjectile(WorldCoordinate source, WorldCoordinate target) {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.VISUAL_EFFECT, "Projectile");

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        var projectileComponent = new ProjectileComponentImpl(entity, worldManager);
        projectileComponent.setSource(source);
        projectileComponent.setTarget(target);
        projectileComponent.setDuration(6);
        projectileComponent.setImageUrl("https://www.pngall.com/wp-content/uploads/14/Blue-Circle-Transparent.png");
        entity.addComponent(projectileComponent);

        return entity;
    }

    public ParticleComponent createParticle() {
        var entity = new EntityImpl(EntityIdentifier.randomIdentifier(), EntityPrefabType.VISUAL_EFFECT, "Particle_Effect");

        var taskComponent = new TaskComponentImpl(entity);
        entity.addComponent(taskComponent);

        var particleComponent = new ParticleComponentImpl(entity, worldManager);
        particleComponent.setDuration(6);
        particleComponent.setImageUrl("https://www.pngall.com/wp-content/uploads/14/Blue-Circle-Transparent.png");
        entity.addComponent(particleComponent);

        return particleComponent;
    }
}
