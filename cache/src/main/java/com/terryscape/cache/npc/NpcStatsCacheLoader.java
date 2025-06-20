package com.terryscape.cache.npc;

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
public class NpcStatsCacheLoader {

    public Map<String, NpcStatsDefinitionImpl> readNpcStatsFromCache() throws IOException {
        var jsonInput = new PathMatchingResourcePatternResolver()
            .getResource(Config.NPC_STATS_CACHE_LOCATION)
            .getContentAsString(StandardCharsets.UTF_8);

        var array = JsonParser
            .parseString(jsonInput)
            .getAsJsonArray();

        return array.asList().stream()
            .map(JsonElement::getAsJsonObject)
            .map(this::createNpcStatsDefinitionFromJson)
            .collect(Collectors.toMap(NpcStatsDefinitionImpl::getId, Function.identity()));
    }

    private NpcStatsDefinitionImpl createNpcStatsDefinitionFromJson(JsonObject jsonObject) {
        return new NpcStatsDefinitionImpl()
            .setId(jsonObject.getAsJsonPrimitive("id").getAsString())
            .setHealth(jsonObject.getAsJsonPrimitive("health").getAsInt())
            .setNpcCombatSkillsDefinition(createNpcCombatSkillsDefinitionFromJson(jsonObject))
            .setNpcCombatBonusesDefinition(createNpcCombatBonusesDefinitionFromJson(jsonObject));
    }

    private NpcCombatSkillsDefinitionImpl createNpcCombatSkillsDefinitionFromJson(JsonObject jsonObject) {
        var skillsJsonObject = jsonObject.getAsJsonObject("skills");

        return new NpcCombatSkillsDefinitionImpl()
            .setAttack(skillsJsonObject.getAsJsonPrimitive("attack").getAsInt())
            .setDefence(skillsJsonObject.getAsJsonPrimitive("defence").getAsInt())
            .setStrength(skillsJsonObject.getAsJsonPrimitive("strength").getAsInt())
            .setMagic(skillsJsonObject.getAsJsonPrimitive("magic").getAsInt())
            .setRange(skillsJsonObject.getAsJsonPrimitive("range").getAsInt());
    }

    private NpcCombatBonusesDefinitionImpl createNpcCombatBonusesDefinitionFromJson(JsonObject jsonObject) {
        var bonusesJsonObject = jsonObject.getAsJsonObject("bonuses");

        return new NpcCombatBonusesDefinitionImpl()
            .setOffensiveStab(bonusesJsonObject.getAsJsonPrimitive("offensiveStab").getAsFloat())
            .setOffensiveSlash(bonusesJsonObject.getAsJsonPrimitive("offensiveSlash").getAsFloat())
            .setOffensiveAir(bonusesJsonObject.getAsJsonPrimitive("offensiveAir").getAsFloat())
            .setOffensiveFire(bonusesJsonObject.getAsJsonPrimitive("offensiveFire").getAsFloat())

            .setDefensiveStab(bonusesJsonObject.getAsJsonPrimitive("defensiveStab").getAsFloat())
            .setDefensiveSlash(bonusesJsonObject.getAsJsonPrimitive("defensiveSlash").getAsFloat())
            .setDefensiveAir(bonusesJsonObject.getAsJsonPrimitive("defensiveAir").getAsFloat())
            .setDefensiveFire(bonusesJsonObject.getAsJsonPrimitive("defensiveFire").getAsFloat())

            .setStrengthMelee(bonusesJsonObject.getAsJsonPrimitive("strengthMelee").getAsFloat())
            .setStrengthMagic(bonusesJsonObject.getAsJsonPrimitive("strengthMagic").getAsFloat());
    }
}
