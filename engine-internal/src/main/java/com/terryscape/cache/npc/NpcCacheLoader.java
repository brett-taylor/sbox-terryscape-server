package com.terryscape.cache.npc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.game.combat.DamageType;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class NpcCacheLoader {

    public Map<String, NpcDefinitionImpl> readNpcsFromCache(Map<String, NpcStatsDefinition> npcStats) throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.NPC_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(jsonObject -> createNpcDefinitionFromJson(npcStats, jsonObject))
            .collect(Collectors.toMap(NpcDefinitionImpl::getId, Function.identity()));
    }

    private NpcDefinitionImpl createNpcDefinitionFromJson(Map<String, NpcStatsDefinition> npcStats, JsonObject jsonObject) {
        var npcDefinition = new NpcDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setName(jsonObject.getAsJsonPrimitive("name").getAsString())
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString())
            .setInteractable(jsonObject.getAsJsonPrimitive("interactable").getAsBoolean())
            .setAttackable(jsonObject.getAsJsonPrimitive("attackable").getAsBoolean())
            .setStatsDefinition(getNpcStatsDefinition(npcStats, jsonObject));

        setAppearanceType(npcDefinition, jsonObject);

        var combatLevel = jsonObject.has("combatLevel") ? jsonObject.getAsJsonPrimitive("combatLevel").getAsInt() : 1;
        npcDefinition.setCombatLevel(combatLevel);

        var combatDamageType = DamageType.TYPELESS;
        if (jsonObject.has("combatDamageType")) {
            var combatDamageTypeAsString = jsonObject.getAsJsonPrimitive("combatDamageType").getAsString();
            combatDamageType = Enum.valueOf(DamageType.class, combatDamageTypeAsString);
        }
        npcDefinition.setCombatDamageType(combatDamageType);

        return npcDefinition;
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

    private NpcStatsDefinition getNpcStatsDefinition(Map<String, NpcStatsDefinition> npcStats, JsonObject jsonObject) {
        if (!jsonObject.has("stats")) {
            return new DefaultNpcStatsDefinitionImpl();
        }

        var npcStatsId = jsonObject.getAsJsonPrimitive("stats").getAsString();
        if (!npcStats.containsKey(npcStatsId)) {
            throw new RuntimeException("Npc wanted npc stats with id %s which didn't exist".formatted(npcStatsId));
        }

        return npcStats.get(npcStatsId);
    }
}
