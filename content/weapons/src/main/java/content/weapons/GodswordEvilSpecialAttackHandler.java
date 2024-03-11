package content.weapons;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.specialattack.SpecialAttackHandler;
import com.terryscape.game.specialattack.SpecialAttackHandlerUtils;

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

        attacker.getEntity().getComponent(PlayerChatComponent.class).ifPresent(playerChatComponent -> {
            playerChatComponent.sendOverheadText("Raarrrrrgggggghhhhhhh!");
        });

        var accuracyRoll = specialAttackHandlerUtils.rollStandardAccuracyHitChance(attacker, victim, DamageType.SLASH);
        if (!accuracyRoll) {
            specialAttackHandlerUtils.showStandardMissedHit(victim, DamageType.SLASH);
            return;
        }

        var damageAmountRollOne = specialAttackHandlerUtils.rollStandardDamageHit(attacker);
        var damageAmountRollTwo = specialAttackHandlerUtils.rollStandardDamageHit(attacker);
        specialAttackHandlerUtils.showStandardHit(victim, Math.max(damageAmountRollOne, damageAmountRollTwo), DamageType.SLASH);
    }

    @Override
    public float getSpecialAttackPowerNeeded() {
        return 50f;
    }
}
