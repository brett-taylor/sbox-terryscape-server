package com.terryscape.cache.npc;

import com.terryscape.game.combat.health.DamageType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;

public interface NpcDefinition {

    String getId();

    String getName();

    String getDescription();

    boolean isInteractable();

    boolean isAttackable();

    NpcDefinitionNpcAppearanceType getAppearanceType();

    Optional<NpcDefinitionSimpleNpc> getSimpleNpc();

    CombatStats getCombatStats();
    List<Pair<DamageType,Integer>> getAttackBonuses();
    List<Pair<DamageType,Integer>> getDefenseBonuses();

}
