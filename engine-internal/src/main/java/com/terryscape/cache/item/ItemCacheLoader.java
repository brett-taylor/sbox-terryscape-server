package com.terryscape.cache.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.game.combat.health.AttackType;
import com.terryscape.game.combat.health.DamageType;
import com.terryscape.game.equipment.EquipmentSlot;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
        ItemType itemType = ItemType.valueOf(jsonObject.getAsJsonPrimitive("itemType").getAsString());
        ItemDefinitionImpl item = switch (itemType){
            case WEAPON -> createWeaponDefinitionFromJson(jsonObject);
            case CLOTHING -> createClothingDefinitionFromJson(jsonObject);
            default -> new ItemDefinitionImpl();
        };

        return item
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setName(jsonObject.getAsJsonPrimitive("name").getAsString())
            .setDescription(jsonObject.getAsJsonPrimitive("description").getAsString());
    }

    private ItemDefinitionImpl createClothingDefinitionFromJson(JsonObject jsonObject) {
        var equipmentSlot = EquipmentSlot.valueOf(jsonObject.getAsJsonPrimitive("equipmentSlot").getAsString());

        List<Pair<DamageType, Integer>> defenseBonuses = new ArrayList<>();
        if(jsonObject.has("bonuses")){
            var bonuses = jsonObject.getAsJsonObject("bonuses");

            bonuses.entrySet()
                    .stream()
                    .map(x -> {
                        var damageType = DamageType.valueOf(x.getKey());
                        return new ImmutablePair(damageType, x.getValue().getAsInt());
                    })
                    .forEach(defenseBonuses::add);
        }


        return new ClothingDefinitionImpl()
                .setEquipmentSlot(equipmentSlot)
                .setBonuses(defenseBonuses);
    }

    private List<String> deseralisePotentialStringArray(JsonObject jsonObject, String entry){
        JsonElement element = jsonObject.get(entry);
        if(element.isJsonArray()){
            return element.getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList();
        } else {
            return List.of(jsonObject.getAsJsonPrimitive(entry).getAsString());
        }
    }

    private ItemDefinitionImpl createWeaponDefinitionFromJson(JsonObject jsonObject) {
        var mainAttackAnimations = deseralisePotentialStringArray(jsonObject, "animationMainHandAttack");
        var offAttackAnimations = deseralisePotentialStringArray(jsonObject, "animationOffHandAttack");

        return new WeaponDefinitionImpl()
                .setAttackAnimations(mainAttackAnimations, true)
                .setAttackAnimations(offAttackAnimations, false)
                .setRange(jsonObject.getAsJsonPrimitive("range").getAsInt())
                .setAttackDelay(jsonObject.getAsJsonPrimitive("attackDelay").getAsInt())
                .setAttributeBonus(jsonObject.getAsJsonPrimitive("attributeBonus").getAsInt())
                .setDamageType(DamageType.valueOf(jsonObject.getAsJsonPrimitive("damageType").getAsString()))
                .setPrimaryAttribute(AttackType.valueOf(jsonObject.getAsJsonPrimitive("primaryAttribute").getAsString()));
    }

}