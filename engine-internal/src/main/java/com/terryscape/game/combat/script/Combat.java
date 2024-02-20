package com.terryscape.game.combat.script;

import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.game.combat.CharacterStatsImpl;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.player.PlayerComponent;

import java.util.Random;

public class Combat {
    public static boolean slap(long currentTick, CombatComponent victim, Entity attacker, WeaponDefinition weapon, boolean mainHand) {
        if(!weapon.attack(currentTick)) {
            return false;
        }

        var victimStats = victim.getEntity().getComponentOrThrow(CharacterStatsImpl.class);
        var attackerStats = attacker.getComponentOrThrow(CharacterStatsImpl.class);

        var weaponDamageType = weapon.getDamageType();
        double victimEvasion = victimStats.GetEvasion(weaponDamageType);
        double attackerAccuracy = attackerStats.GetAccuracy(weaponDamageType);

        var rand = new Random();
        double hitChance = 1;
        if (victimEvasion > 0) {
            hitChance = attackerAccuracy / victimEvasion;
        }
        var hitAttempt = rand.nextDouble();

        var hit = hitChance > hitAttempt;

        var damageAmount = weapon.getPrimaryAttributeBonus() + attackerStats.GetProficiency(weaponDamageType);
        var randomDouble = rand.nextDouble();
        var positiveBias = Math.pow(randomDouble, 0.4);

        damageAmount = (int) (Math.ceil(damageAmount * positiveBias) + 0.05f);

        var damage = new DamageInformation()
                .setHit(hit)
                .setIsUsingMainHand(mainHand)
                .setType(weaponDamageType)
                .setAmount(damageAmount);

        victim.getEntity().getComponentOrThrow(HealthComponent.class).takeDamage(damage);

        return true;
    }
}
