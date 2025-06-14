package content.weapons;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
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

    private final ItemDefinition godswordEvilItemDefinition;

    @Inject
    public GodswordEvilSpecialAttackHandler(SpecialAttackHandlerUtils specialAttackHandlerUtils,
                                            @Named("godsword_evil") ItemDefinition godswordEvilItemDefinition) {

        this.specialAttackHandlerUtils = specialAttackHandlerUtils;
        this.godswordEvilItemDefinition = godswordEvilItemDefinition;
    }

    @Override
    public Set<ItemDefinition> getItems() {
        return Set.of(godswordEvilItemDefinition);
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
