package content.startingzone.npcs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.event.EventSystem;
import com.terryscape.event.type.OnGameStartedSystemEvent;
import com.terryscape.game.loottable.LootTable;
import com.terryscape.game.loottable.LootTableItem;
import com.terryscape.game.loottable.LootTableProvider;

import java.util.ArrayList;
import java.util.Set;

@Singleton
public class GoblinLootTableProvider implements LootTableProvider {

    private NpcDefinition goblin;
    private NpcDefinition goblinWarrior;
    private NpcDefinition goblinShaman;
    private NpcDefinition goblinChief;

    private ItemDefinition goldCoin;
    private ItemDefinition steelFullHelm;
    private ItemDefinition steelPlatebody;
    private ItemDefinition steelPlatelegs;
    private ItemDefinition steelBoots;
    private ItemDefinition steelGloves;

    private ItemDefinition basicScimitar;
    private ItemDefinition basicSword;
    private ItemDefinition godswordEvil;
    private ItemDefinition godswordRighteous;

    private ItemDefinition foodFish;
    private ItemDefinition foodPie;
    private ItemDefinition foodChicken;
    private ItemDefinition foodPotato;

    @Inject
    public GoblinLootTableProvider(CacheLoader cacheLoader, EventSystem eventSystem) {
        eventSystem.subscribe(OnGameStartedSystemEvent.class, ignored -> {
            goblin = cacheLoader.getNpcDefinition("goblin");
            goblinWarrior = cacheLoader.getNpcDefinition("goblin_warrior");
            goblinShaman = cacheLoader.getNpcDefinition("goblin_shaman");
            goblinChief = cacheLoader.getNpcDefinition("goblin_chief");

            goldCoin = cacheLoader.getItemDefinition("gold_coin");

            steelFullHelm = cacheLoader.getItemDefinition("steel_full_helm");
            steelPlatebody = cacheLoader.getItemDefinition("steel_platebody");
            steelPlatelegs = cacheLoader.getItemDefinition("steel_platelegs");
            steelBoots = cacheLoader.getItemDefinition("steel_boots");
            steelGloves = cacheLoader.getItemDefinition("steel_gloves");

            basicScimitar = cacheLoader.getItemDefinition("basic_scimitar");
            basicSword = cacheLoader.getItemDefinition("basic_sword");
            godswordEvil = cacheLoader.getItemDefinition("godsword_evil");
            godswordRighteous = cacheLoader.getItemDefinition("godsword_righteous");

            foodFish = cacheLoader.getItemDefinition("food_fish");
            foodPie = cacheLoader.getItemDefinition("food_pie");
            foodChicken = cacheLoader.getItemDefinition("food_chicken");
            foodPotato = cacheLoader.getItemDefinition("food_potato");
        });
    }

    @Override
    public Set<String> getNpcIds() {
        return Set.of("goblin", "goblin_warrior", "goblin_shaman", "goblin_chief");
    }

    @Override
    public LootTable getLootTable(NpcDefinition npcDefinition) {
        var guaranteedDrops = new ArrayList<LootTableItem>();

        if (npcDefinition == goblin) {
            guaranteedDrops.add(LootTableItem.randomAmount(goldCoin, 5, 30));
        } else if (npcDefinition == goblinWarrior) {
            guaranteedDrops.add(LootTableItem.randomAmount(goldCoin, 20, 100));
        } else if (npcDefinition == goblinShaman) {
            guaranteedDrops.add(LootTableItem.randomAmount(goldCoin, 100, 1000));
        } else if (npcDefinition == goblinChief) {
            guaranteedDrops.add(LootTableItem.randomAmount(goldCoin, 1000, 4000));
        }

        var optionalDrops = new ArrayList<LootTableItem>();

        if (npcDefinition == goblin || npcDefinition == goblinWarrior || npcDefinition == goblinShaman) {
            optionalDrops.add(LootTableItem.one(steelGloves));
            optionalDrops.add(LootTableItem.one(steelBoots));

            optionalDrops.add(LootTableItem.one(basicScimitar));
            optionalDrops.add(LootTableItem.one(basicSword));

            optionalDrops.add(LootTableItem.randomAmount(foodFish, 1, 3));
            optionalDrops.add(LootTableItem.randomAmount(foodPie, 1, 3));
            optionalDrops.add(LootTableItem.randomAmount(foodChicken, 1, 3));
            optionalDrops.add(LootTableItem.randomAmount(foodPotato, 1, 3));
        }

        if (npcDefinition == goblinWarrior || npcDefinition == goblinShaman) {
            optionalDrops.add(LootTableItem.one(steelFullHelm));
            optionalDrops.add(LootTableItem.one(steelPlatelegs));
            optionalDrops.add(LootTableItem.one(steelPlatebody));
        }

        if (npcDefinition == goblinChief) {
            optionalDrops.add(LootTableItem.one(godswordEvil));
            optionalDrops.add(LootTableItem.one(godswordRighteous));
        }

        return new LootTable(guaranteedDrops, optionalDrops);
    }
}
