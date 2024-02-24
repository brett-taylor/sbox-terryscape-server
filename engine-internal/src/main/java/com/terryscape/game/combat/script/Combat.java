package com.terryscape.game.combat.script;

import com.terryscape.cache.item.WeaponDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.game.combat.CharacterStatsImpl;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.combat.health.HealthComponent;

import java.util.Random;

public class Combat {
    private static final Random rand = new Random();

    public static boolean slap(long currentTick, Entity victim, Entity attacker, WeaponDefinition weapon, boolean mainHand) {
        if(!weapon.attack(currentTick)) {
            return false;
        }

        var victimStats = victim.getComponentOrThrow(CharacterStatsImpl.class);
        var attackerStats = attacker.getComponentOrThrow(CharacterStatsImpl.class);

        var weaponDamageType = weapon.getDamageType();
        var hitChance = HitChance(attackerStats, victimStats, weaponDamageType);
        var hit = AttemptHit(hitChance);

        var maximumDamage = attackerStats.getEvasion(weaponDamageType);
        var damageAmount = DamageAmount(maximumDamage);
        
        var damage = new DamageInformation()
                .setHit(hit)
                .setIsUsingMainHand(mainHand)
                .setType(weaponDamageType)
                .setAmount(damageAmount);

        victim.getComponentOrThrow(HealthComponent.class).takeDamage(damage);

        return true;
    }

    public static boolean AttemptHit(double hitChance){
        return hitChance > rand.nextDouble();
    }

    public static int DamageAmount(int maximumDamage){
        var randomDouble = rand.nextDouble();
        var positiveBias = Math.pow(randomDouble, 0.4);

        return  (int) (Math.ceil(maximumDamage * positiveBias) + 0.005f);
    }

    public static double HitChance(CharacterStatsImpl attacker, CharacterStatsImpl victim, DamageType weaponDamageType) {
        return HitChance(attacker, victim, weaponDamageType, 100);
    }
    public static double HitChance(CharacterStatsImpl attackerStats, CharacterStatsImpl victimStats, DamageType weaponDamageType, double attackerModifier) {
        attackerModifier /= 100;
        double victimEvasion = victimStats.getEvasion(weaponDamageType);
        double attackerAccuracy = attackerStats.getAccuracy(weaponDamageType);

        attackerAccuracy *= attackerModifier;

        double hitChance = 1;
        if (victimEvasion > 0) {
            hitChance = attackerAccuracy / victimEvasion;
        }

        return hitChance;
    }
}
