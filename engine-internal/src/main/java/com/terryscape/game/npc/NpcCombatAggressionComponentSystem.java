package com.terryscape.game.npc;

import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.OnAttackEntityEvent;
import com.terryscape.game.combat.OnAttackedEntityEvent;
import com.terryscape.game.movement.MovementComponent;

@Singleton
public class NpcCombatAggressionComponentSystem extends ComponentSystem<NpcCombatAggressionComponent> {

    private static final int NO_LONGER_AGGRESSIVE_TIMER = 20;

    @Override
    public Class<NpcCombatAggressionComponent> forComponentType() {
        return NpcCombatAggressionComponent.class;
    }

    @Override
    public void onRegistered(Entity entity, NpcCombatAggressionComponent component) {
        component.getEntity().subscribe(OnAttackedEntityEvent.class, onAttackedEntityEvent -> onAttacked(onAttackedEntityEvent, component));
        component.getEntity().subscribe(OnAttackEntityEvent.class, ignored -> onAttack(component));
    }

    @Override
    public void onTick(Entity entity, NpcCombatAggressionComponent component) {
        var combatComponent = entity.getComponentOrThrow(CombatComponent.class);
        if (combatComponent.isInCombat()) {
            component.setTicksSinceLastAttacked(component.getTicksSinceLastAttacked() + 1);
            component.setTicksSinceLastAttack(component.getTicksSinceLastAttack() + 1);
        }

        if (component.getTicksSinceLastAttack() >= NO_LONGER_AGGRESSIVE_TIMER && component.getTicksSinceLastAttacked() >= NO_LONGER_AGGRESSIVE_TIMER) {
            component.setTicksSinceLastAttacked(0);
            component.setTicksSinceLastAttack(0);

            combatComponent.stopAttacking();
            entity.getComponentOrThrow(MovementComponent.class).stop();
        }
    }

    private void onAttack(NpcCombatAggressionComponent component) {
        component.setTicksSinceLastAttack(0);
    }

    private void onAttacked(OnAttackedEntityEvent onAttackedEntityEvent, NpcCombatAggressionComponent component) {
        component.setTicksSinceLastAttacked(0);

        var combatComponent = component.getEntity().getComponentOrThrow(CombatComponent.class);
        if (combatComponent.isInCombat()) {
            return;
        }

        // We don't want npcs to attack back straight away so make them wait one tick
        combatComponent.ensureCooldownOfAtLeast(2);
        combatComponent.attack(onAttackedEntityEvent.getAttacker());
    }
}
