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

    public Map<String, ItemDefinitionImpl> readItemsFromCache() throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.ITEM_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(this::createItemDefinitionFromJson)
            .collect(Collectors.toMap(ItemDefinitionImpl::getId, Function.identity()));
    }

    private ItemDefinitionImpl createItemDefinitionFromJson(JsonObject jsonObject) {
        return new ItemDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setName(jsonObject.getAsJsonPrimitive("name").getAsString())
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString())
            .setStackable(jsonObject.getAsJsonPrimitive("stackable").getAsBoolean())
            .setEquipItemDefinition(createEquipItemDefinitionFromJson(jsonObject));
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

}