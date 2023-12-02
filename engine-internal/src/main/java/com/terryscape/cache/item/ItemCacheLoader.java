package com.terryscape.cache.item;

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
public class ItemCacheLoader {

    public Map<String, ItemDefinitionImpl> readItemsFromCache() throws IOException {
        try (var input = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(Config.ITEM_CACHE_LOCATION)))) {
            var reader = JsonParser.parseReader(input);
            var array = reader.getAsJsonArray();

            return array.asList().stream()
                .map(JsonElement::getAsJsonObject)
                .map(this::createItemDefinitionFromJson)
                .collect(Collectors.toMap(ItemDefinitionImpl::getId, Function.identity()));
        }
    }

    private ItemDefinitionImpl createItemDefinitionFromJson(JsonObject jsonObject) {
        return new ItemDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setName(jsonObject.getAsJsonPrimitive("name").getAsString())
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString())
            .setAnimationMainHandAttack(jsonObject.getAsJsonPrimitive("animationMainHandAttack").getAsString())
            .setAnimationOffHandAttack(jsonObject.getAsJsonPrimitive("animationOffHandAttack").getAsString());
    }

}
