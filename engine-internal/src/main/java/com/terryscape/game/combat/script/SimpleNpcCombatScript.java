package com.terryscape.game.combat.script;

import com.terryscape.game.combat.CombatBonusesProviderComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.CombatSkillsProviderComponent;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.npc.NpcCombatBonusesProviderComponent;
import com.terryscape.game.npc.NpcCombatSkillsProviderComponent;
import com.terryscape.game.npc.NpcComponent;

public class SimpleNpcCombatScript implements CombatScript {

    private final NpcComponent npcComponent;

    private final AnimationComponent animationComponent;

    private final NpcCombatSkillsProviderComponent npcCombatSkillsProviderComponent;

    private final NpcCombatBonusesProviderComponent npcCombatBonusesProviderComponent;

    public SimpleNpcCombatScript(NpcComponent npcComponent) {
        this.npcComponent = npcComponent;
        this.animationComponent = npcComponent.getEntity().getComponentOrThrow(AnimationComponent.class);
        this.npcCombatSkillsProviderComponent = npcComponent.getEntity().getComponentOrThrow(NpcCombatSkillsProviderComponent.class);
        this.npcCombatBonusesProviderComponent = npcComponent.getEntity().getComponentOrThrow(NpcCombatBonusesProviderComponent.class);
    }

    @Override
    public int range() {
        return 1;
    }

    @Override
    public boolean attack(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll) {
        var damageType = npcComponent.getNpcDefinition().getCombatDamageType();
        var damageInformation = new DamageInformation().setType(damageType);
        var victimSkills = victim.getEntity().getComponentOrThrow(CombatSkillsProviderComponent.class);
        var victimBonuses = victim.getEntity().getComponentOrThrow(CombatBonusesProviderComponent.class);
        var didPassAccuracyRoll = combatDiceRoll.rollHitChance(damageType, npcCombatSkillsProviderComponent, npcCombatBonusesProviderComponent, victimSkills, victimBonuses);

        if (didPassAccuracyRoll) {
            damageInformation.setAmount(combatDiceRoll.rollDamage(npcCombatSkillsProviderComponent, npcCombatBonusesProviderComponent));
        } else {
            damageInformation.setAmount(0).setBlocked(true);
        }

        attacker.ensureCooldownOfAtLeast(4);
        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damageInformation);
        animationComponent.playAnimation("attack");
        return true;
    }
}
