package com.terryscape.game.npc;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.OnAttackedEntityEvent;
import com.terryscape.game.movement.MovementComponent;

public class NpcCombatAggressionComponent extends BaseEntityComponent {

    private static final int NO_LONGER_AGGRESSIVE_TIMER = 12;

    private final CombatComponent combatComponent;

    private final MovementComponent movementComponent;

    private int ticksSinceLastAttack;

    public NpcCombatAggressionComponent(Entity entity, CombatComponent combatComponent, MovementComponent movementComponent) {
        super(entity);

        this.combatComponent = combatComponent;
        this.movementComponent = movementComponent;

        getEntity().subscribe(OnAttackedEntityEvent.class, this::onAttacked);
    }

    @Override
    public void tick() {
        super.tick();

        if (combatComponent.isInCombat()) {
            ticksSinceLastAttack += 1;
        }

        if (ticksSinceLastAttack >= NO_LONGER_AGGRESSIVE_TIMER) {
            ticksSinceLastAttack = 0;
            combatComponent.stopAttacking();
            movementComponent.stop();
        }
    }

    private void onAttacked(OnAttackedEntityEvent onAttackedEntityEvent) {
        // We don't want npcs to attack back straight away so make them wait one tick
        combatComponent.ensureCooldownOfAtLeast(2);
        combatComponent.attack(onAttackedEntityEvent.getAttacker());

        ticksSinceLastAttack = 0;
    }
}
