package com.terryscape.game.npc;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.OnAttackEntityEvent;
import com.terryscape.game.combat.OnAttackedEntityEvent;
import com.terryscape.game.movement.MovementComponent;

public class NpcCombatAggressionComponent extends BaseEntityComponent {

    private static final int NO_LONGER_AGGRESSIVE_TIMER = 20;

    private final CombatComponent combatComponent;

    private final MovementComponent movementComponent;

    private int ticksSinceLastAttack;

    private int ticksSinceLastAttacked;

    public NpcCombatAggressionComponent(CombatComponent combatComponent, MovementComponent movementComponent) {
        this.combatComponent = combatComponent;
        this.movementComponent = movementComponent;

        getEntity().subscribe(OnAttackedEntityEvent.class, this::onAttacked);
        getEntity().subscribe(OnAttackEntityEvent.class, this::onAttack);
    }

    @Override
    public void tick() {
        super.tick();

        if (combatComponent.isInCombat()) {
            ticksSinceLastAttack += 1;
            ticksSinceLastAttacked += 1;
        }

        if (ticksSinceLastAttack >= NO_LONGER_AGGRESSIVE_TIMER && ticksSinceLastAttacked >= NO_LONGER_AGGRESSIVE_TIMER) {
            ticksSinceLastAttack = 0;
            ticksSinceLastAttacked = 0;

            combatComponent.stopAttacking();
            movementComponent.stop();
        }
    }

    private void onAttacked(OnAttackedEntityEvent onAttackedEntityEvent) {
        ticksSinceLastAttacked = 0;

        if (combatComponent.isInCombat()) {
            return;
        }

        // We don't want npcs to attack back straight away so make them wait one tick
        combatComponent.ensureCooldownOfAtLeast(2);
        combatComponent.attack(onAttackedEntityEvent.getAttacker());
    }

    private void onAttack(OnAttackEntityEvent onAttackEntityEvent) {
        ticksSinceLastAttack = 0;
    }
}
