package com.terryscape.game.combat;

import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.projectile.ProjectileFactory;

public interface CombatHit {

    void onRegistered(CombatComponent attacker, CombatComponent victim, ProjectileFactory projectileFactory);

    int calculateHitDelayTicks(CombatComponent attacker, CombatComponent victim);

    void executeHit(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll);

}
