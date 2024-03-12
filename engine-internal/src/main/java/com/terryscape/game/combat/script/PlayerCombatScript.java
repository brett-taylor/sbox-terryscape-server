package com.terryscape.game.combat.script;

import com.terryscape.game.combat.*;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.player.PlayerBonusesProviderComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.player.PlayerSkillsComponent;
import com.terryscape.game.specialattack.SpecialAttackDispatcher;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.WorldClock;

import java.util.List;

public class PlayerCombatScript implements CombatScript {

    private final int STANDARD_ATTACK_GLOBAL_COOLDOWN = 4;

    private final WorldClock worldClock;

    private final PlayerComponent playerComponent;

    private final AnimationComponent animationComponent;

    private final PlayerSkillsComponent playerSkillsComponent;

    private final PlayerBonusesProviderComponent playerBonusesProviderComponent;

    private final SpecialAttackDispatcher specialAttackDispatcher;

    private long lastMainHandAttackTime;

    private long lastOffHandAttackTime;

    public PlayerCombatScript(WorldClock worldClock, PlayerComponent playerComponent, SpecialAttackDispatcher specialAttackDispatcher) {
        this.worldClock = worldClock;
        this.playerComponent = playerComponent;
        this.specialAttackDispatcher = specialAttackDispatcher;

        this.animationComponent = playerComponent.getEntity().getComponentOrThrow(AnimationComponent.class);
        this.playerSkillsComponent = playerComponent.getEntity().getComponentOrThrow(PlayerSkillsComponent.class);
        this.playerBonusesProviderComponent = playerComponent.getEntity().getComponentOrThrow(PlayerBonusesProviderComponent.class);
    }

    @Override
    public int range() {
        return 1;
    }

    @Override
    public boolean attack(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll) {
        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);

        if (mainHand.isPresent() || offHand.isPresent()) {
            return handleAttackWithWeapon(attacker, victim, combatDiceRoll);
        } else {
            doHit(victim, combatDiceRoll, DamageType.STAB, pickUnarmedAttackAnimationId(), true);
            attacker.ensureCooldownOfAtLeast(STANDARD_ATTACK_GLOBAL_COOLDOWN);
            return true;
        }
    }

    private String pickUnarmedAttackAnimationId() {
        var animations = List.of(
            "Unarmed_Attack_Kick_L1",
            "Unarmed_Attack_Kick_L2",
            "Unarmed_Attack_Kick_R1",
            "Unarmed_Attack_Kick_R2",
            "Unarmed_Attack_L1",
            "Unarmed_Attack_L2",
            "Unarmed_Attack_L3",
            "Unarmed_Attack_R1",
            "Unarmed_Attack_R2",
            "Unarmed_Attack_R3"
        );

        return RandomUtil.randomCollection(animations);
    }

    private boolean handleAttackWithWeapon(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll) {
        var playerEquipment = playerComponent.getEquipment();

        var mainHandAttackSpeed = 6;
        if (playerEquipment.getSlot(EquipmentSlot.MAIN_HAND).isPresent()) {
            mainHandAttackSpeed = playerEquipment.getSlotOrThrow(EquipmentSlot.MAIN_HAND).getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow().getAttackSpeed();
        }

        var offHandAttackSpeed = 6;
        if (playerEquipment.getSlot(EquipmentSlot.OFF_HAND).isPresent()) {
            offHandAttackSpeed = playerEquipment.getSlotOrThrow(EquipmentSlot.OFF_HAND).getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow().getAttackSpeed();
        }

        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        var isMainHandOffCooldown = lastMainHandAttackTime + mainHandAttackSpeed < worldClock.getNowTick();

        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
        var isOffHandOffCooldown = lastOffHandAttackTime + offHandAttackSpeed < worldClock.getNowTick();

        if (mainHand.isPresent() && isMainHandOffCooldown) {
            var weaponDefinition = mainHand.get().getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow();

            if (playerComponent.wantsToSpecialAttack() && specialAttackDispatcher.shouldSpecialAttack(attacker, mainHand.get().getItemDefinition())) {
                specialAttackDispatcher.invoke(attacker, victim);
                return true;
            } else {
                playerComponent.setWantsToSpecialAttack(false);
            }

            doHit(victim, combatDiceRoll, weaponDefinition.getDamageType(), weaponDefinition.getMainHandAttackAnimation(), true);
            attacker.ensureCooldownOfAtLeast(STANDARD_ATTACK_GLOBAL_COOLDOWN);
            return true;
        }

        if (offHand.isPresent() && isOffHandOffCooldown) {
            var weaponDefinition = offHand.get().getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow();
            doHit(victim, combatDiceRoll, weaponDefinition.getDamageType(), weaponDefinition.getOffHandAttackAnimation(), false);
            attacker.ensureCooldownOfAtLeast(STANDARD_ATTACK_GLOBAL_COOLDOWN);
            return true;
        }

        return false;
    }

    private void doHit(CombatComponent victim, CombatDiceRoll combatDiceRoll, DamageType damageType, String animationIdToPlay, boolean isMainHand) {
        var victimSkills = victim.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var victimBonuses = victim.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var didPassAccuracyRoll = combatDiceRoll.rollHitChance(damageType, playerSkillsComponent, playerBonusesProviderComponent, victimSkills, victimBonuses);

        var damageInformation = new DamageInformation()
            .setType(damageType);

        if (didPassAccuracyRoll) {
            damageInformation.setAmount(combatDiceRoll.rollDamage(playerSkillsComponent, playerBonusesProviderComponent));
        } else {
            damageInformation.setAmount(0).setBlocked(true);
        }

        if (isMainHand) {
            lastMainHandAttackTime = worldClock.getNowTick();
        } else {
            lastOffHandAttackTime = worldClock.getNowTick();
        }

        animationComponent.playAnimation(animationIdToPlay);
        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damageInformation);
    }
}
