package com.terryscape.cache.npc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import com.terryscape.Config;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString());

        setAppearanceType(npcDefinition, jsonObject);

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
}
