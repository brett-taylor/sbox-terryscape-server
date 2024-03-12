package content.startingzone.npcs;

import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.combat.combathit.StandardMagicCombatHit;

public class GoblinShamanCombatScript implements CombatScript {

    private final int ATTACK_COOLDOWN = 3;

    private CombatComponent attacker;

    @Override
    public void setOwner(CombatComponent combatComponent) {
        attacker = combatComponent;
    }

    @Override
    public int range() {
        return 10;
    }

    @Override
    public void attack(CombatComponent victim) {
        var meleeHit = new StandardMagicCombatHit(DamageType.FIRE, "attack");
        attacker.registerAttack(victim, meleeHit);

        attacker.ensureCooldownOfAtLeast(ATTACK_COOLDOWN);
    }

}
