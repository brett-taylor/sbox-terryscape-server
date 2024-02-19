package com.terryscape.game.combat.script;

import com.terryscape.cache.item.WeaponDefinitionImpl;
import com.terryscape.game.combat.CharacterStatsImpl;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.health.AttackType;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.WorldClock;

import java.util.List;
import java.util.Random;

public class PlayerCombatScript implements CombatScript {
    private final WorldClock worldClock;
    private final PlayerComponent playerComponent;
    private final MovementComponent movementComponent;
    private final AnimationComponent animationComponent;
    private WeaponDefinitionImpl unarmed;
    private long nextAttackOpportunity;
    private int attackDelay;

    public PlayerCombatScript(WorldClock worldClock, PlayerComponent playerComponent) {
        this.worldClock = worldClock;
        this.playerComponent = playerComponent;
        this.movementComponent = playerComponent.getEntity().getComponentOrThrow(MovementComponent.class);
        this.animationComponent = playerComponent.getEntity().getComponentOrThrow(AnimationComponent.class);
        this.attackDelay = 3;

        unarmed = new WeaponDefinitionImpl()
                .setPrimaryAttribute(AttackType.MELEE)
                .setDamageType(DamageType.SMASH)
                .setAttributeBonus(5)
                .setAttackAnimation(pickUnarmedAttackAnimationId())
                .setAttackDelay(attackDelay);
    }

    @Override
    public boolean isInRange(CombatComponent victim) {
        var victimMovementComponent = victim.getEntity().getComponentOrThrow(MovementComponent.class);
        return movementComponent.getWorldCoordinate().distance(victimMovementComponent.getWorldCoordinate()) <= 2f;
    }

    @Override
    public boolean attack(CombatComponent victim) {
        if (nextAttackOpportunity >= worldClock.getNowTick()) {
            return false;
        }

        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);

        if (mainHand.isPresent() || offHand.isPresent()) {
            return handleAttackWithWeapon(victim);
        }

        slap(victim, unarmed);
        return true;
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

    private boolean handleAttackWithWeapon(CombatComponent victim) {
        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        if (mainHand.isPresent()) {
            WeaponDefinitionImpl weapon = (WeaponDefinitionImpl) mainHand.get();
            if(weapon.attack(worldClock.getNowTick())) {
                slap(victim, weapon);
                return true;
            }
        }

        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
        if (offHand.isPresent()) {
            WeaponDefinitionImpl weapon = (WeaponDefinitionImpl) offHand.get();
            if(weapon.attack(worldClock.getNowTick())) {
                slap(victim, weapon);
                return true;
            }
        }

        return false;
    }

    private void slap(CombatComponent victim, WeaponDefinitionImpl weapon) {
        nextAttackOpportunity = worldClock.getNowTick() + attackDelay;

        var victimStats = victim.getEntity().getComponentOrThrow(CharacterStatsImpl.class);
        var attackerStats = playerComponent.getEntity().getComponentOrThrow(CharacterStatsImpl.class);

        var weaponDamageType = weapon.getDamageType();
        double victimEvasion = victimStats.GetEvasion(weaponDamageType);
        double attackerAccuracy = attackerStats.GetAccuracy(weaponDamageType);

        var rand = new Random();
        var hitChance = attackerAccuracy / victimEvasion;
        var hitAttempt = rand.nextDouble();

        System.out.println(hitChance + " " + hitAttempt + " " + attackerAccuracy + " " + victimEvasion);

        if(hitChance < hitAttempt) return;

        var damageAmount = weapon.getPrimaryAttributeBonus() + attackerStats.GetProficiency(weaponDamageType);
        var randomDouble = rand.nextDouble();
        var positiveBias = Math.pow(randomDouble, 0.4);

        damageAmount = (int) (Math.ceil(damageAmount * positiveBias) + 0.05f);

        var damage = new DamageInformation().setAmount(damageAmount);
        animationComponent.playAnimation(weapon.getAttackAnimation());

        damage.setType(weaponDamageType);

        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damage);
    }
}
