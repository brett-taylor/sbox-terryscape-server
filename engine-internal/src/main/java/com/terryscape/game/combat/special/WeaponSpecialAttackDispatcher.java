package com.terryscape.game.combat.special;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.game.combat.CombatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class WeaponSpecialAttackDispatcher {

    private static final Logger LOGGER = LogManager.getLogger(WeaponSpecialAttackDispatcher.class);

    private final Map<String, WeaponSpecialAttackHandler> weaponSpecialAttackHandlers;

    @Inject
    public WeaponSpecialAttackDispatcher(Set<WeaponSpecialAttackHandler> handlers) {
        this.weaponSpecialAttackHandlers = new HashMap<>();

        handlers.forEach(this::registerSingleWeaponSpecialAttackInteractionHandler);
        LOGGER.info("Registered special attack handlers for {} weapons.", weaponSpecialAttackHandlers.size());
    }

    public void dispatchSpecialWeaponAttack(WeaponDefinition weaponDefinition, CombatComponent attacker, CombatComponent victim) {
        var handler = weaponSpecialAttackHandlers.get(weaponDefinition.getId());
        if (handler == null) {
            LOGGER.error("No special attack handler found for weapon with id {}.", weaponDefinition.getId());
            return;
        }

        handler.attack(attacker, victim);
    }

    private void registerSingleWeaponSpecialAttackInteractionHandler(WeaponSpecialAttackHandler handler) {
        if (weaponSpecialAttackHandlers.containsKey(handler.getItemId())) {
            throw new RuntimeException("A WeaponSpecialAttackHandler can't be registered to weapon %s as it already has one".formatted(handler.getItemId()));
        }

        weaponSpecialAttackHandlers.put(handler.getItemId(), handler);
    }
}
