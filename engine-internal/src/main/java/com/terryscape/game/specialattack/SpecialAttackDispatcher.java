package com.terryscape.game.specialattack;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.player.PlayerComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class SpecialAttackDispatcher {

    private static final Logger LOGGER = LogManager.getLogger(SpecialAttackDispatcher.class);

    private final Map<String, SpecialAttackHandler> specialAttackHandlers;

    @Inject
    public SpecialAttackDispatcher(Set<SpecialAttackHandler> handlers) {
        this.specialAttackHandlers = new HashMap<>();

        handlers.forEach(this::registerSingleWeaponSpecialAttackInteractionHandler);
        LOGGER.info("Registered special attack handlers for {} weapons.", specialAttackHandlers.size());
    }

    public boolean shouldSpecialAttack(CombatComponent attacker, ItemDefinition itemDefinition) {
        var isSpecialWeapon = specialAttackHandlers.containsKey(itemDefinition.getId());
        if (!isSpecialWeapon) {
            return false;
        }

        var specialAttackPowerRequired = specialAttackHandlers.get(itemDefinition.getId()).getSpecialAttackPowerNeeded();
        var player = attacker.getEntity().getComponentOrThrow(PlayerComponent.class);

        return specialAttackPowerRequired <= player.getSpecialAttackPower();
    }

    public void invoke(CombatComponent attacker, CombatComponent victim) {
        var attackerPlayer = attacker.getEntity().getComponentOrThrow(PlayerComponent.class);
        var mainHand = attackerPlayer.getEquipment().getSlotOrThrow(EquipmentSlot.MAIN_HAND);

        var handler = specialAttackHandlers.get(mainHand.getItemDefinition().getId());
        handler.attack(attacker, victim);

        var newSpecialAttackPower = attackerPlayer.getSpecialAttackPower() - handler.getSpecialAttackPowerNeeded();
        attackerPlayer.setSpecialAttackPower(newSpecialAttackPower);
    }

    private void registerSingleWeaponSpecialAttackInteractionHandler(SpecialAttackHandler handler) {
        for (var itemId : handler.getItemIds()) {
            if (specialAttackHandlers.containsKey(itemId)) {
                throw new RuntimeException("A SpecialAttackHandler can't be registered to weapon %s as it already has one".formatted(itemId));
            }

            specialAttackHandlers.put(itemId, handler);
        }
    }
}
