package com.terryscape.game.combat.script;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.world.WorldClock;

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
        var isMainHandOffCooldown = lastMainHandAttackTime + 6 < worldClock.getNowTick();

        if (mainHand.isPresent() && isMainHandOffCooldown) {
            slap(victim, true, mainHand.get());
            return true;
        }

        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
        var isOffHandOffCooldown = lastOffHandAttackTime + 6 < worldClock.getNowTick();

        if (offHand.isPresent() && isOffHandOffCooldown) {
            slap(victim, false, offHand.get());
            return true;
        }

        return false;
    }

    private void slap(CombatComponent victim, boolean mainHand, ItemDefinition item) {
        lastAttackTime = worldClock.getNowTick();

        var damage = new DamageInformation().setAmount(1);

        if (mainHand) {
            lastMainHandAttackTime = worldClock.getNowTick();
            animationComponent.playAnimation(item.getAnimationMainHandAttack());
            damage.setType(DamageType.MELEE_MAIN_HAND);
        } else {
            lastOffHandAttackTime = worldClock.getNowTick();
            animationComponent.playAnimation(item.getAnimationOffHandAttack());
            damage.setType(DamageType.MELEE_OFF_HAND);
        }

        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damage);
    }
}
