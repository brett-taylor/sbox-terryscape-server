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
public class GodswordEvilSpecialAttackHandler implements SpecialAttackHandler {

    private final SpecialAttackHandlerUtils specialAttackHandlerUtils;

    @Inject
    public GodswordEvilSpecialAttackHandler(SpecialAttackHandlerUtils specialAttackHandlerUtils) {
        this.specialAttackHandlerUtils = specialAttackHandlerUtils;
    }

    @Override
    public Set<String> getItemIds() {
        return Set.of("godsword_evil");
    }

    @Override
    public void attack(CombatComponent attacker, CombatComponent victim) {
        var hit = new StandardMeleeSpecialAttackCombatHit();

        hit.setAttackAnimationId("2Hand_Sword_Attack3");

        hit.setOnAccuracyRoll(() -> {
            attacker.getEntity().getComponent(PlayerChatComponent.class)
                .ifPresent(playerChatComponent -> playerChatComponent.sendOverheadText("Glory to the Evil!"));

            return specialAttackHandlerUtils.rollStandardAccuracyHitChance(attacker, victim, DamageType.SLASH);
        });

        hit.setOnMiss(() -> specialAttackHandlerUtils.showStandardMissedHit(victim, DamageType.SLASH));

        hit.setOnHit(() -> {
            var damageAmountRollOne = MathsUtil.floorToInt(specialAttackHandlerUtils.rollStandardDamageHit(attacker, DamageType.SLASH) * 1.5f);
            var damageAmountRollTwo = MathsUtil.floorToInt(specialAttackHandlerUtils.rollStandardDamageHit(attacker, DamageType.SLASH) * 1.5f);
            specialAttackHandlerUtils.showStandardHit(victim, Math.max(damageAmountRollOne, damageAmountRollTwo), DamageType.SLASH);
        });

        attacker.registerAttack(victim, hit);
    }

    @Override
    public float getSpecialAttackPowerNeeded() {
        return 25f;
    }
}
