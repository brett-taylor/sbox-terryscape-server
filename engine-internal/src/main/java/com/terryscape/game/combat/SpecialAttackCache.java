package com.terryscape.game.combat;

import com.terryscape.entity.Entity;
import com.terryscape.game.combat.health.DamageInformation;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.combat.script.Combat;
import com.terryscape.game.movement.AnimationComponentImpl;
import org.apache.commons.lang3.function.TriConsumer;

public enum SpecialAttackCache {
        BASIC_SWORD(SpecialAttackCache::executeBasicSword);
        public final TriConsumer<Entity, Entity, SpecialAttackTrigger> specialAttack;
        SpecialAttackCache(TriConsumer<Entity, Entity, SpecialAttackTrigger> execute) {
                specialAttack = execute;
        }

        public static void executeBasicSword(Entity attacker, Entity defender, SpecialAttackTrigger attackTrigger){
                var attackerStats = attacker.getComponentOrThrow(CharacterStatsImpl.class);
                var defenderStats = defender.getComponentOrThrow(CharacterStatsImpl.class);
                var attackerAnimation = attacker.getComponentOrThrow(AnimationComponentImpl.class);
                var defenderHealth = defender.getComponentOrThrow(HealthComponent.class);

                var attackerDamageType = attackTrigger.getDamageType();
                var damageBonus = (100 + attackTrigger.getDamageBonus()) / 100.0;
                var damage = (int)(attackerStats.GetProficiency(attackerDamageType) * damageBonus); //Weapon
                damage = Combat.DamageAmount(damage);

                var hitChance = Combat.HitChance(attackerStats, defenderStats, attackerDamageType, 100 + attackTrigger.getAccuracyBonus());
                var didHit = Combat.AttemptHit(hitChance);

                var damageInformation = new DamageInformation()
                        .setType(attackerDamageType)
                        .setIsUsingMainHand(true)
                        .setAmount(damage)
                        .setHit(didHit);

                defenderHealth.takeDamage(damageInformation);
                attackerAnimation.playAnimation(attackTrigger.getAnimationName());
        }
}
