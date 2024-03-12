package com.terryscape.game.combat.script;

import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.hit.StandardMeleeCombatHit;
import com.terryscape.game.npc.NpcComponent;

public class BasicNpcCombatScript implements CombatScript {

    private final int STANDARD_ATTACK_GLOBAL_COOLDOWN = 4;

    private CombatComponent attacker;

    private NpcComponent attackerNpc;

    @Override
    public void setOwner(CombatComponent combatComponent) {
        attacker = combatComponent;
        attackerNpc = combatComponent.getEntity().getComponentOrThrow(NpcComponent.class);
    }

    @Override
    public int range() {
        return 1;
    }

    @Override
    public void attack(CombatComponent victim) {
        var damageType = attackerNpc.getNpcDefinition().getCombatDamageType();

        var meleeHit = new StandardMeleeCombatHit(damageType, "attack");
        attacker.registerAttack(victim, meleeHit);

        attacker.ensureCooldownOfAtLeast(STANDARD_ATTACK_GLOBAL_COOLDOWN);
    }
}
