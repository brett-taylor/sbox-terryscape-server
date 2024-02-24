package com.terryscape.game.combat.script;

import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.cache.item.WeaponDefinitionImpl;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.SpecialBarImpl;
import com.terryscape.game.combat.health.AttackType;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.special.WeaponSpecialAttackDispatcher;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.world.WorldClock;

import java.util.List;

public class PlayerCombatScript implements CombatScript {
    private final WorldClock worldClock;

    private final PlayerComponent playerComponent;

    private final MovementComponent movementComponent;

    private final AnimationComponent animationComponent;

    private final WeaponDefinitionImpl unarmed;

    private final WeaponSpecialAttackDispatcher weaponSpecialAttackDispatcher;

    private long nextAttackOpportunity;
    private final int attackDelay;
    private SpecialBarImpl specialBar;

    public PlayerCombatScript(WorldClock worldClock, PlayerComponent playerComponent, WeaponSpecialAttackDispatcher weaponSpecialAttackDispatcher) {
        this.worldClock = worldClock;
        this.playerComponent = playerComponent;
        this.movementComponent = playerComponent.getEntity().getComponentOrThrow(MovementComponent.class);
        this.animationComponent = playerComponent.getEntity().getComponentOrThrow(AnimationComponent.class);
        this.specialBar = playerComponent.getEntity().getComponentOrThrow(SpecialBarImpl.class);
        this.attackDelay = 3;

        this.weaponSpecialAttackDispatcher = weaponSpecialAttackDispatcher;

        var unarmedAttackAnimations = List.of(
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


        unarmed = new WeaponDefinitionImpl()
                .setPrimaryAttribute(AttackType.MELEE)
                .setDamageType(DamageType.SMASH)
                .setAttributeBonus(5)
                .setAttackAnimations(unarmedAttackAnimations)
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

        // Should do special attack, if so pass attack onto the special attack dispatcher.
        /**if(attemptSpecialAttack(victim)) {
            return true;
        }*/
        var specialAttack = specialBar.getSlot();
        if (specialAttack != null) {
            var weapon = (WeaponDefinitionImpl) playerComponent.getEquipment().getSlot(specialAttack).orElseThrow();
            var self = playerComponent.getEntity().getComponent(CombatComponent.class).orElseThrow();
            if(weapon.canAttack(worldClock.getNowTick())) {
                if (weaponSpecialAttackDispatcher.dispatchSpecialWeaponAttack(weapon, self, victim)) {
                    nextAttackOpportunity = worldClock.getNowTick() + attackDelay;
                    return true;
                }
            }
        }

        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);


        if (mainHand.isPresent() || offHand.isPresent()) {
            return handleAttackWithWeapon(victim);
        }

        return attack(victim, unarmed, true);
    }

    private boolean handleAttackWithWeapon(CombatComponent victim) {
        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        if (mainHand.isPresent()) {
            var weapon = (WeaponDefinition) mainHand.get();
            if(attack(victim, weapon, true)) {
                return true;
            }
        }

        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
        if (offHand.isPresent()) {
            var weapon = (WeaponDefinition) offHand.get();
            return attack(victim, weapon, false);
        }

        return false;
    }

    private boolean attack(CombatComponent victim, WeaponDefinition weapon, boolean mainHand) {
        var currentTick = worldClock.getNowTick();
        var didAttack = Combat.slap(currentTick, victim.getEntity(), playerComponent.getEntity(), weapon, mainHand);

        if(didAttack) {
            nextAttackOpportunity = currentTick + attackDelay;
            animationComponent.playAnimation(weapon.getAttackAnimation(mainHand));
        }

        return didAttack;
    }
}
