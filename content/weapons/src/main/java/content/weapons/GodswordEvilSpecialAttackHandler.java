package content.weapons;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.specialattack.SpecialAttackHandler;
import com.terryscape.game.specialattack.SpecialAttackHandlerUtils;
import com.terryscape.maths.MathsUtil;

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
        attacker.getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation("2Hand_Sword_Attack3");

        attacker.getEntity().getComponent(PlayerChatComponent.class)
            .ifPresent(playerChatComponent -> playerChatComponent.sendOverheadText("Glory to the Evil!"));

        var accuracyRoll = specialAttackHandlerUtils.rollStandardAccuracyHitChance(attacker, victim, DamageType.SLASH);
        if (!accuracyRoll) {
            specialAttackHandlerUtils.showStandardMissedHit(victim, DamageType.SLASH);
            return;
        }

        var damageAmountRollOne = MathsUtil.floorToInt(specialAttackHandlerUtils.rollStandardDamageHit(attacker) * 1.5f);
        var damageAmountRollTwo = MathsUtil.floorToInt(specialAttackHandlerUtils.rollStandardDamageHit(attacker) * 1.5f);
        specialAttackHandlerUtils.showStandardHit(victim, Math.max(damageAmountRollOne, damageAmountRollTwo), DamageType.SLASH);
    }

    @Override
    public float getSpecialAttackPowerNeeded() {
        return 25f;
    }
}
