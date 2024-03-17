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
            .collect(Collectors.toMap(ItemStatsDefinitionImpl::getId, Function.identity()));
    }

    private ItemStatsDefinitionImpl createItemStatsDefinitionFromJson(JsonObject jsonObject) {
        return new ItemStatsDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setWeight(jsonObject.getAsJsonPrimitive("weight").getAsFloat())

            .setOffensiveStab(jsonObject.getAsJsonPrimitive("offensiveStab").getAsFloat())
            .setOffensiveSlash(jsonObject.getAsJsonPrimitive("offensiveSlash").getAsFloat())
            .setOffensiveAir(jsonObject.getAsJsonPrimitive("offensiveAir").getAsFloat())
            .setOffensiveFire(jsonObject.getAsJsonPrimitive("offensiveFire").getAsFloat())
            .setOffensiveArrow(jsonObject.getAsJsonPrimitive("offensiveArrow").getAsFloat())

            .setDefensiveStab(jsonObject.getAsJsonPrimitive("defensiveStab").getAsFloat())
            .setDefensiveSlash(jsonObject.getAsJsonPrimitive("defensiveSlash").getAsFloat())
            .setDefensiveAir(jsonObject.getAsJsonPrimitive("defensiveAir").getAsFloat())
            .setDefensiveFire(jsonObject.getAsJsonPrimitive("defensiveFire").getAsFloat())
            .setDefensiveArrow(jsonObject.getAsJsonPrimitive("defensiveArrow").getAsFloat())

            .setStrengthMelee(jsonObject.getAsJsonPrimitive("strengthMelee").getAsFloat())
            .setStrengthMagic(jsonObject.getAsJsonPrimitive("strengthMagic").getAsFloat())
            .setStrengthRange(jsonObject.getAsJsonPrimitive("strengthRange").getAsFloat());
    }

}
