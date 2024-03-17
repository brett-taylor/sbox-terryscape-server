package com.terryscape.cache.projectile;

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
public class ProjectileCacheLoader {

    public Map<String, ProjectileDefinitionImpl> readProjectilesFromCache() throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.PROJECTILE_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(this::createProjectileDefinitionFromJson)
            .collect(Collectors.toMap(ProjectileDefinition::getId, Function.identity()));
    }

    private ProjectileDefinitionImpl createProjectileDefinitionFromJson(JsonObject jsonObject) {
        return new ProjectileDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString());
    }

}
