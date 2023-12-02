package com.terryscape.cache.npc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import com.terryscape.Config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class NpcCacheLoader {

    public Map<String, NpcDefinitionImpl> readNpcsFromCache() throws IOException {
        try (var input = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(Config.NPC_CACHE_LOCATION)))) {
            var reader = JsonParser.parseReader(input);
            var array = reader.getAsJsonArray();

            return array.asList().stream()
                .map(JsonElement::getAsJsonObject)
                .map(this::createNpcDefinitionFromJson)
                .collect(Collectors.toMap(NpcDefinitionImpl::getId, Function.identity()));
        }
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
