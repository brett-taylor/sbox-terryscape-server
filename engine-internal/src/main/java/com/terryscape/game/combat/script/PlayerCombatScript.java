package com.terryscape.game.combat.script;

import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
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

public class PlayerCombatScript implements CombatScript {

    private final WorldClock worldClock;

    private final PlayerComponent playerComponent;

    private final MovementComponent movementComponent;

    private final AnimationComponent animationComponent;

    private long lastAttackTime;

    private long lastMainHandAttackTime;

    private long lastOffHandAttackTime;

    public PlayerCombatScript(WorldClock worldClock, PlayerComponent playerComponent) {
        this.worldClock = worldClock;
        this.playerComponent = playerComponent;
        this.movementComponent = playerComponent.getEntity().getComponentOrThrow(MovementComponent.class);
        this.animationComponent = playerComponent.getEntity().getComponentOrThrow(AnimationComponent.class);
    }

    @Override
    public boolean isInRange(CombatComponent victim) {
        var victimMovementComponent = victim.getEntity().getComponentOrThrow(MovementComponent.class);
        return movementComponent.getWorldCoordinate().distance(victimMovementComponent.getWorldCoordinate()) <= 2f;
    }

    @Override
    public boolean attack(CombatComponent victim) {
        if (lastAttackTime + 3 > worldClock.getNowTick()) {
            return false;
        }

        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);

        if (mainHand.isPresent() || offHand.isPresent()) {
            return handleAttackWithWeapon(victim);
        }

        slap(victim, true, pickUnarmedAttackAnimationId());
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
        var isMainHandOffCooldown = lastMainHandAttackTime + 6 < worldClock.getNowTick();

        if (mainHand.isPresent() && isMainHandOffCooldown) {
            var animation = mainHand.get().getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow().getMainHandAttackAnimation();
            slap(victim, true, animation);
            return true;
        }

        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
        var isOffHandOffCooldown = lastOffHandAttackTime + 6 < worldClock.getNowTick();

        if (offHand.isPresent() && isOffHandOffCooldown) {
            var animation = offHand.get().getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow().getOffHandAttackAnimation();
            slap(victim, false, animation);
            return true;
        }

        return false;
    }

    private void slap(CombatComponent victim, boolean mainHand, String animationIdToPlay) {
        lastAttackTime = worldClock.getNowTick();

        var damage = new DamageInformation().setAmount(1);
        animationComponent.playAnimation(animationIdToPlay);

        if (mainHand) {
            lastMainHandAttackTime = worldClock.getNowTick();
            damage.setType(DamageType.MELEE_MAIN_HAND);
        } else {
            lastOffHandAttackTime = worldClock.getNowTick();
            damage.setType(DamageType.MELEE_OFF_HAND);
        }

        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damage);
    }
}
