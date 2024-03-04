package com.terryscape.cache.item;

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
public class ItemStatsCacheLoader {

    public Map<String, ItemStatsDefinitionImpl> readItemStatsFromCache() throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.ITEM_STATS_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(this::createItemStatsDefinitionFromJson)
            .collect(Collectors.toMap(ItemStatsDefinition::getId, Function.identity()));
    }

    private ItemStatsDefinitionImpl createItemStatsDefinitionFromJson(JsonObject jsonObject) {
        return new ItemStatsDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setWeight(jsonObject.getAsJsonPrimitive("weight").getAsFloat())
            .setOffensiveStab(jsonObject.getAsJsonPrimitive("offensive_stab").getAsFloat())
            .setOffensiveSlash(jsonObject.getAsJsonPrimitive("offensive_slash").getAsFloat())
            .setDefensiveStab(jsonObject.getAsJsonPrimitive("defensive_stab").getAsFloat())
            .setDefensiveSlash(jsonObject.getAsJsonPrimitive("defensive_slash").getAsFloat());
    }

}
