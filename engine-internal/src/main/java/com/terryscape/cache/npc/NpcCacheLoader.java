package com.terryscape.cache.npc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.game.combat.health.DamageType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class NpcCacheLoader {

    public Map<String, NpcDefinitionImpl> readNpcsFromCache() throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.NPC_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(this::createNpcDefinitionFromJson)
            .collect(Collectors.toMap(NpcDefinitionImpl::getId, Function.identity()));
    }

    private NpcDefinitionImpl createNpcDefinitionFromJson(JsonObject jsonObject) {
        var npcDefinition = new NpcDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setName(jsonObject.getAsJsonPrimitive("name").getAsString())
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString())
            .setInteractable(jsonObject.getAsJsonPrimitive("interactable").getAsBoolean())
            .setAttackable(jsonObject.getAsJsonPrimitive("attackable").getAsBoolean());

        setAppearanceType(npcDefinition, jsonObject);

        if(jsonObject.has("stats")){
            setStats(npcDefinition, jsonObject.getAsJsonObject("stats"));
        }

        return npcDefinition;
    }

    private void setStats(NpcDefinitionImpl npcDefinition, JsonObject jsonObject) {
        CombatStats stats = new CombatStats();

        var primaryDamageType = DamageType.TYPELESS; //The default
        if(jsonObject.has("primaryDamageType")){
            primaryDamageType = DamageType.valueOf(jsonObject.getAsJsonPrimitive("primaryDamageType").getAsString());
        }

        var combatStats = jsonObject.getAsJsonObject("combatStats");
        stats.Attack = combatStats.getAsJsonPrimitive("attack").getAsInt();
        stats.Defense = combatStats.getAsJsonPrimitive("defense").getAsInt();
        stats.Mage = combatStats.getAsJsonPrimitive("mage").getAsInt();
        stats.Range = combatStats.getAsJsonPrimitive("range").getAsInt();
        stats.Melee = combatStats.getAsJsonPrimitive("melee").getAsInt();

        List<Pair<DamageType, Integer>> attackBonuses = new ArrayList<>();
        if(jsonObject.has("attackBonuses")){
            var bonuses = jsonObject.getAsJsonObject("attackBonuses");
            bonuses.entrySet()
                    .stream()
                    .map(x -> {
                        var damageType = DamageType.valueOf(x.getKey());
                        return new ImmutablePair(damageType, x.getValue().getAsInt());
                    })
                    .forEach(attackBonuses::add);
        }

        List<Pair<DamageType, Integer>> defenseBonuses = new ArrayList<>();
        if(jsonObject.has("defenseBonuses")){
            var bonuses = jsonObject.getAsJsonObject("defenseBonuses");
            bonuses.entrySet()
                    .stream()
                    .map(x -> {
                        var damageType = DamageType.valueOf(x.getKey());
                        return new ImmutablePair(damageType, x.getValue().getAsInt());
                    })
                    .forEach(defenseBonuses::add);
        }

        npcDefinition
                .setDamageType(primaryDamageType)
                .setCombatStats(stats)
                .setAttackBonuses(attackBonuses)
                .setDefenseBonuses(defenseBonuses);
    }

    private void setAppearanceType(NpcDefinitionImpl npcDefinition, JsonObject jsonObject) {
        var simpleNpcJsonObject = jsonObject.getAsJsonObject("simpleNpc");
        var humanoidNpcJsonObject = jsonObject.getAsJsonObject("humanoidNpc");

        if (simpleNpcJsonObject != null) {
            var simpleNpc = new NpcDefinitionSimpleNpcImpl()
                .setVariants(simpleNpcJsonObject.getAsJsonObject("variants").keySet().stream().toList());

            npcDefinition.setSimpleNpc(simpleNpc);
            npcDefinition.setAppearanceType(NpcDefinitionNpcAppearanceType.SIMPLE);
        }

        if (humanoidNpcJsonObject != null) {
            npcDefinition.setAppearanceType(NpcDefinitionNpcAppearanceType.HUMANOID);
        }

        if (npcDefinition.getAppearanceType() == null) {
            throw new RuntimeException("Could not calculate appearance type for %s".formatted(npcDefinition.getId()));
        }
    }
}
