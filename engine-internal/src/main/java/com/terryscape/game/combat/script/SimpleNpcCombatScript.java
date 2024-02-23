package com.terryscape.game.combat.script;

import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.cache.item.WeaponDefinitionImpl;
import com.terryscape.game.combat.CharacterStatsImpl;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.world.WorldClock;

import java.util.List;
import java.util.Random;

public class SimpleNpcCombatScript implements CombatScript {
    private final WorldClock worldClock;

    private final NpcComponent npcComponent;

    private final MovementComponent movementComponent;

    private final AnimationComponent animationComponent;

    private long lastAttackTime;

    private final int attackDelay;

    private final WeaponDefinition weapon;

    public SimpleNpcCombatScript(WorldClock worldClock, NpcComponent npcComponent, DamageType damageType) {
        this.worldClock = worldClock;
        this.npcComponent = npcComponent;
        this.movementComponent = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class);
        this.animationComponent = npcComponent.getEntity().getComponentOrThrow(AnimationComponent.class);

        attackDelay = 4;

        var attacks = List.of("attack");
        this.weapon = new WeaponDefinitionImpl()
                .setAttackAnimations(attacks)
                .setAttackDelay(attackDelay)
                .setDamageType(damageType);
    }

    @Override
    public boolean isInRange(CombatComponent victim) {
        var victimMovementComponent = victim.getEntity().getComponentOrThrow(MovementComponent.class);
        return movementComponent.getWorldCoordinate().distance(victimMovementComponent.getWorldCoordinate()) <= 2f;
    }

    @Override
    public boolean attack(CombatComponent victim) {
        var currentTick = worldClock.getNowTick();
        //You don't actually need to track this due to the delay built into the weapon
        if (lastAttackTime + attackDelay > worldClock.getNowTick()) {
            return false;
        }

        var didAttack = Combat.slap(currentTick, victim.getEntity(), npcComponent.getEntity(), weapon, true);

        if(didAttack) {
            lastAttackTime = currentTick;
            animationComponent.playAnimation(weapon.getAttackAnimation(true));
        }

        return didAttack;
    }
}
