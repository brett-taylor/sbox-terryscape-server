package com.terryscape.game.combat.hit;

import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.movement.AnimationComponent;

public class StandardMeleeCombatHit extends StandardCombatFormulaHit {

    private final DamageType damageType;

    private final String attackAnimationId;

    public StandardMeleeCombatHit(DamageType damageType, String attackAnimationId) {
        this.damageType = damageType;
        this.attackAnimationId = attackAnimationId;
    }

    @Override
    public void onRegistered(CombatComponent attacker, CombatComponent victim) {
        attacker.getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation(attackAnimationId);
    }

    @Override
    public int calculateHitDelayTicks(CombatComponent attacker, CombatComponent victim) {
        return 0;
    }

    @Override
    protected DamageType getDamageType() {
        return damageType;
    }
}
