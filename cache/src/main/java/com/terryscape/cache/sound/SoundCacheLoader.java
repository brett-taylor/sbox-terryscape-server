package com.terryscape.cache.sound;

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
public class SoundCacheLoader {

    public Map<String, SoundDefinitionImpl> readSoundsFromCache() throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.SOUND_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(this::createSoundDefinitionFromJson)
            .collect(Collectors.toMap(SoundDefinition::getId, Function.identity()));
    }

    private SoundDefinitionImpl createSoundDefinitionFromJson(JsonObject jsonObject) {
        return new SoundDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString());
    }

}
