package content.weapons;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.specialattack.SpecialAttackHandler;
import com.terryscape.game.specialattack.SpecialAttackHandlerUtils;
import com.terryscape.maths.MathsUtil;
import content.combathit.StandardMeleeSpecialAttackCombatHit;

import java.util.Set;

@Singleton
public class GodswordRighteousSpecialAttackHandler implements SpecialAttackHandler {

    private final SpecialAttackHandlerUtils specialAttackHandlerUtils;

    @Inject
    public GodswordRighteousSpecialAttackHandler(SpecialAttackHandlerUtils specialAttackHandlerUtils) {
        this.specialAttackHandlerUtils = specialAttackHandlerUtils;
    }

    @Override
    public Set<String> getItemIds() {
        return Set.of("godsword_righteous");
    }

    @Override
    public void attack(CombatComponent attacker, CombatComponent victim) {
        var hit = new StandardMeleeSpecialAttackCombatHit();

        hit.setAttackAnimationId("2Hand_Sword_Attack3");

        hit.setOnAccuracyRoll(() -> {
            var accuracyRollOne = specialAttackHandlerUtils.rollStandardAccuracyHitChance(attacker, victim, DamageType.SLASH);
            var accuracyRollTwo = specialAttackHandlerUtils.rollStandardAccuracyHitChance(attacker, victim, DamageType.SLASH);

            attacker.getEntity().getComponent(PlayerChatComponent.class)
                .ifPresent(playerChatComponent -> playerChatComponent.sendOverheadText("Glory to the Righteous!"));

            return accuracyRollOne || accuracyRollTwo;
        });

        hit.setOnMiss(() -> specialAttackHandlerUtils.showStandardMissedHit(victim, DamageType.SLASH));

        hit.setOnHit(() -> {
            var damageAmountRoll = MathsUtil.floorToInt(specialAttackHandlerUtils.rollStandardDamageHit(attacker) * 1.5f);
            specialAttackHandlerUtils.showStandardHit(victim, damageAmountRoll, DamageType.SLASH);
        });

        attacker.registerAttack(victim, hit);
    }

    @Override
    public float getSpecialAttackPowerNeeded() {
        return 25f;
    }
}
