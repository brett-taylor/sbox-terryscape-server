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
        return new NpcDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setName(jsonObject.getAsJsonPrimitive("name").getAsString())
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString())
            .setSimpleNpc(createSimpleNpcFromJson(jsonObject.getAsJsonObject("simpleNpc")));
    }

    private NpcDefinitionSimpleNpcImpl createSimpleNpcFromJson(JsonObject jsonObject) {
        return new NpcDefinitionSimpleNpcImpl()
            .setVariants(jsonObject.getAsJsonObject("variants").keySet().stream().toList());
    }
}
