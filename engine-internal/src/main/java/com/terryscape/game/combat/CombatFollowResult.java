package com.terryscape.game.combat;

public class CombatFollowResult {

    public static CombatFollowResult isInRangeAndLineOfSight() {
        return new CombatFollowResult(false, true);
    }

    public static CombatFollowResult notInRangeOrLineOfSight() {
        return new CombatFollowResult(false, false);
    }

    public static CombatFollowResult stopAttacking() {
        return new CombatFollowResult(true, false);
    }

    private final boolean shouldStopAttacking;

    private final boolean inRangeAndLos;

    private CombatFollowResult(boolean shouldStopAttacking, boolean isInRangeAndLineOfSight) {
        this.shouldStopAttacking = shouldStopAttacking;
        this.inRangeAndLos = isInRangeAndLineOfSight;
    }

    public boolean shouldStopAttacking() {
        return shouldStopAttacking;
    }

    public boolean canAttack() {
        return inRangeAndLos;
    }
}
