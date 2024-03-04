package com.terryscape.cache.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.game.equipment.EquipmentSlot;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class ItemCacheLoader {

    public Map<String, ItemDefinitionImpl> readItemsFromCache(Map<String, ItemStatsDefinition> itemStats) throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.ITEM_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(jsonObject -> createItemDefinitionFromJson(itemStats, jsonObject))
            .collect(Collectors.toMap(ItemDefinitionImpl::getId, Function.identity()));
    }

    private ItemDefinitionImpl createItemDefinitionFromJson(Map<String, ItemStatsDefinition> itemStats, JsonObject jsonObject) {
        return new ItemDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setName(jsonObject.getAsJsonPrimitive("name").getAsString())
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString())
            .setStackable(jsonObject.getAsJsonPrimitive("stackable").getAsBoolean())
            .setEquipItemDefinition(createEquipItemDefinitionFromJson(jsonObject))
            .setItemStatsDefinition(getItemStatsDefinition(itemStats, jsonObject));
    }

    private EquipItemDefinition createEquipItemDefinitionFromJson(JsonObject jsonObject) {
        if (!jsonObject.has("equip")) {
            return null;
        }

        var equipItemJsonObject = jsonObject.getAsJsonObject("equip");
        return new EquipItemDefinitionImpl()
            .setEquipmentSlot(Enum.valueOf(EquipmentSlot.class, equipItemJsonObject.getAsJsonPrimitive("slot").getAsString()))
            .setWeaponItemDefinition(createWeaponItemDefinitionFromJson(equipItemJsonObject));
    }

    private WeaponItemDefinition createWeaponItemDefinitionFromJson(JsonObject jsonObject) {
        if (!jsonObject.has("weapon")) {
            return null;
        }

        var weaponItemJsonObject = jsonObject.getAsJsonObject("weapon");
        return new WeaponItemDefinitionImpl()
            .setMainHandAttackAnimation(weaponItemJsonObject.getAsJsonPrimitive("mainHandAttackAnimation").getAsString())
            .setOffHandAttackAnimation(weaponItemJsonObject.getAsJsonPrimitive("offHandAttackAnimation").getAsString());
    }

    private ItemStatsDefinition getItemStatsDefinition(Map<String, ItemStatsDefinition> itemStats, JsonObject jsonObject) {
        if (!jsonObject.has("stats")) {
            return new DefaultItemStatsDefinition();
        }

        var itemStatsId = jsonObject.getAsJsonPrimitive("stats").getAsString();
        if (!itemStats.containsKey(itemStatsId)) {
            throw new RuntimeException("Item wanted item stats with id %s which didn't exist".formatted(itemStatsId));
        }

        return itemStats.get(itemStatsId);
    }

}