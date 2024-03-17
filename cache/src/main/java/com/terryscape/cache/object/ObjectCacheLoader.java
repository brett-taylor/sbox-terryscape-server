package com.terryscape.cache.object;

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
public class ObjectCacheLoader {

    public Map<String, ObjectDefinitionImpl> readObjectsFromCache() throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.OBJECT_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(this::createObjectDefinitionFromJson)
            .collect(Collectors.toMap(ObjectDefinition::getId, Function.identity()));
    }

    private ObjectDefinitionImpl createObjectDefinitionFromJson(JsonObject jsonObject) {
        return new ObjectDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setName(jsonObject.getAsJsonPrimitive("name").getAsString())
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString())
            .setInteractable(jsonObject.getAsJsonPrimitive("interactable").getAsBoolean());
    }

}
