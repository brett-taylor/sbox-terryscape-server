package com.terryscape.game.combat;

public class PendingCombatHit {

    private final CombatComponent attacker;

    private final CombatComponent victim;

    private final CombatHit combatHit;

    private int ticksLeft;

    public PendingCombatHit(CombatComponent attacker, CombatComponent victim, CombatHit combatHit) {
        this.attacker = attacker;
        this.victim = victim;
        this.combatHit = combatHit;

        ticksLeft = combatHit.calculateHitDelayTicks(attacker, victim);
    }

    public void tick() {
        ticksLeft -= 1;
    }

    public boolean shouldExecute() {
        return ticksLeft <= 0;
    }

    public CombatComponent getVictim() {
        return victim;
    }

    public CombatHit getCombatHit() {
        return combatHit;
    }
}
