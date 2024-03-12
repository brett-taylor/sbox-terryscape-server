package content.combathit;

import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatHit;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.movement.AnimationComponent;

import java.util.function.Supplier;

public class StandardMeleeSpecialAttackCombatHit implements CombatHit {

    private String attackAnimationId;

    private Supplier<Boolean> onAccuracyRoll;

    private Runnable onMiss;

    private Runnable onHit;

    public void setAttackAnimationId(String attackAnimationId) {
        this.attackAnimationId = attackAnimationId;
    }

    public void setOnAccuracyRoll(Supplier<Boolean> onAccuracyRoll) {
        this.onAccuracyRoll = onAccuracyRoll;
    }

    public void setOnMiss(Runnable onMiss) {
        this.onMiss = onMiss;
    }

    public void setOnHit(Runnable onHit) {
        this.onHit = onHit;
    }

    @Override
    public int getHitDelayTicks() {
        return 0;
    }

    @Override
    public void executeHit(CombatComponent attacker, CombatComponent victim, CombatDiceRoll combatDiceRoll) {
        attacker.getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation(attackAnimationId);

        var accuracyRoll = onAccuracyRoll.get();

        if (!accuracyRoll) {
            onMiss.run();
            return;
        }

        onHit.run();
    }
}
