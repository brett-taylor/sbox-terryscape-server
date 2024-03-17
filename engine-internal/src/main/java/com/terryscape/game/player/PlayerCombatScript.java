package com.terryscape.game.player;

import com.terryscape.cache.item.WeaponItemDefinition;
import com.terryscape.game.combat.CombatComponent;
import com.terryscape.game.combat.CombatHit;
import com.terryscape.game.combat.CombatScript;
import com.terryscape.game.combat.DamageType;
import com.terryscape.game.combat.combathit.StandardMagicCombatHit;
import com.terryscape.game.combat.combathit.StandardMeleeCombatHit;
import com.terryscape.game.combat.combathit.StandardRangeCombatHit;
import com.terryscape.game.equipment.EquipmentSlot;
import com.terryscape.game.specialattack.SpecialAttackDispatcher;
import com.terryscape.maths.RandomUtil;
import com.terryscape.game.world.WorldClock;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class PlayerCombatScript implements CombatScript {

    private final int STANDARD_ATTACK_GLOBAL_COOLDOWN = 4;

    private final WorldClock worldClock;

    private final SpecialAttackDispatcher specialAttackDispatcher;

    private CombatComponent attacker;

    private PlayerComponent attackerPlayer;

    private long lastMainHandAttackTime;

    private long lastOffHandAttackTime;

    public PlayerCombatScript(WorldClock worldClock, SpecialAttackDispatcher specialAttackDispatcher) {
        this.worldClock = worldClock;
        this.specialAttackDispatcher = specialAttackDispatcher;
    }

    @Override
    public void setOwner(CombatComponent combatComponent) {
        attacker = combatComponent;
        attackerPlayer = attacker.getEntity().getComponentOrThrow(PlayerComponent.class);
    }

    @Override
    public int range() {
        var playerComponent = attacker.getEntity().getComponentOrThrow(PlayerComponent.class);

        var mainHand = playerComponent.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        var offHand = playerComponent.getEquipment().getSlot(EquipmentSlot.OFF_HAND);

        if (mainHand.isEmpty() && offHand.isEmpty()) {
            return 1;
        }

        if (mainHand.isPresent()) {
            return mainHand.get().getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow().getRange();
        }

        return offHand.get().getItemDefinition().getEquipDefinitionOrThrow().getWeaponDefinitionOrThrow().getRange();
    }

    @Override
    public void attack(CombatComponent victim) {
        var mainHand = attackerPlayer.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        var offHand = attackerPlayer.getEquipment().getSlot(EquipmentSlot.OFF_HAND);

        if (mainHand.isPresent() || offHand.isPresent()) {
            handleArmedAttack(victim);
        } else {
            handleUnarmedAttack(victim);
        }
    }

    private void handleUnarmedAttack(CombatComponent victim) {
        attacker.ensureCooldownOfAtLeast(STANDARD_ATTACK_GLOBAL_COOLDOWN);

        var meleeHit = new StandardMeleeCombatHit(DamageType.STAB, pickUnarmedAttackAnimationId());
        attacker.registerAttack(victim, meleeHit);
    }

    private void handleArmedAttack(CombatComponent victim) {
        var mainHand = attackerPlayer.getEquipment().getSlot(EquipmentSlot.MAIN_HAND);
        if (mainHand.isPresent()) {
            var weapon = mainHand.get()
                .getItemDefinition()
                .getEquipDefinitionOrThrow()
                .getWeaponDefinitionOrThrow();

            var isOffCooldown = lastMainHandAttackTime + weapon.getAttackSpeed() < worldClock.getNowTick();
            if (isOffCooldown) {

                if (attackerPlayer.wantsToSpecialAttack() && specialAttackDispatcher.shouldSpecialAttack(attacker, mainHand.get().getItemDefinition())) {
                    specialAttackDispatcher.invoke(attacker, victim);
                } else {
                    attacker.registerAttack(victim, createCombatHit(weapon, true));
                }

                attackerPlayer.setWantsToSpecialAttack(false);
                attacker.ensureCooldownOfAtLeast(STANDARD_ATTACK_GLOBAL_COOLDOWN);
                lastMainHandAttackTime = worldClock.getNowTick();

                return;
            }
        }

        var offHand = attackerPlayer.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
        if (offHand.isPresent()) {
            var weapon = offHand.get()
                .getItemDefinition()
                .getEquipDefinitionOrThrow()
                .getWeaponDefinitionOrThrow();

            var isOffCooldown = lastOffHandAttackTime + weapon.getAttackSpeed() < worldClock.getNowTick();
            if (isOffCooldown) {
                attacker.registerAttack(victim, createCombatHit(weapon, false));
                attacker.ensureCooldownOfAtLeast(STANDARD_ATTACK_GLOBAL_COOLDOWN);
                lastOffHandAttackTime = worldClock.getNowTick();
            }
        }
    }

    private String pickUnarmedAttackAnimationId() {
        var animations = List.of(
            "Unarmed_Attack_Kick_L1",
            "Unarmed_Attack_Kick_L2",
            "Unarmed_Attack_Kick_R1",
            "Unarmed_Attack_Kick_R2",
            "Unarmed_Attack_L1",
            "Unarmed_Attack_L2",
            "Unarmed_Attack_L3",
            "Unarmed_Attack_R1",
            "Unarmed_Attack_R2",
            "Unarmed_Attack_R3"
        );

        return RandomUtil.randomCollection(animations);
    }

    private CombatHit createCombatHit(WeaponItemDefinition weapon, boolean mainHand) {
        // TODO: Work out a better way of doing this? Maybe its moving weapons into java code and having them just define it? Not sure I love the json

        var attackAnimation = mainHand ? weapon.getMainHandAttackAnimation() : weapon.getOffHandAttackAnimation();

        return switch (weapon.getDamageType()) {
            case STAB, SLASH -> new StandardMeleeCombatHit(weapon.getDamageType(), attackAnimation);

            case AIR, FIRE -> new StandardMagicCombatHit(weapon.getDamageType(), attackAnimation);

            case ARROW -> new StandardRangeCombatHit(weapon.getDamageType(), attackAnimation);

            case TYPELESS -> throw new NotImplementedException();
        };
    }
}
