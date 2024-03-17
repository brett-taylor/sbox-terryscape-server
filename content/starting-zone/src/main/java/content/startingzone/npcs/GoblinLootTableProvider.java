package content.startingzone.npcs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.loottable.LootTable;
import com.terryscape.game.loottable.LootTableItem;
import com.terryscape.game.loottable.LootTableProvider;

import java.util.ArrayList;
import java.util.Set;

@Singleton
public class GoblinLootTableProvider implements LootTableProvider {

    private final NpcDefinition goblin;

    private final NpcDefinition goblinWarrior;

    private final NpcDefinition goblinShaman;

    private final NpcDefinition goblinChief;

    private final ItemDefinition goldCoin;

    private final ItemDefinition steelFullHelm;

    private final ItemDefinition steelPlatebody;

    private final ItemDefinition steelPlatelegs;

    private final ItemDefinition steelBoots;

    private final ItemDefinition steelGloves;

    private final ItemDefinition basicScimitar;

    private final ItemDefinition basicSword;

    private final ItemDefinition godswordEvil;

    private final ItemDefinition godswordRighteous;

    private final ItemDefinition foodFish;

    private final ItemDefinition foodPie;

    private final ItemDefinition foodChicken;

    private final ItemDefinition foodPotato;

    @Inject
    public GoblinLootTableProvider(@Named("goblin") NpcDefinition goblin,
                                   @Named("goblin_warrior") NpcDefinition goblinWarrior,
                                   @Named("goblin_shaman") NpcDefinition goblinShaman,
                                   @Named("goblin_chief") NpcDefinition goblinChief,
                                   @Named("gold_coin") ItemDefinition goldCoin,
                                   @Named("steel_full_helm") ItemDefinition steelFullHelm,
                                   @Named("steel_platebody") ItemDefinition steelPlatebody,
                                   @Named("steel_platelegs") ItemDefinition steelPlatelegs,
                                   @Named("steel_boots") ItemDefinition steelBoots,
                                   @Named("steel_gloves") ItemDefinition steelGloves,
                                   @Named("basic_scimitar") ItemDefinition basicScimitar,
                                   @Named("basic_sword") ItemDefinition basicSword,
                                   @Named("godsword_evil") ItemDefinition godswordEvil,
                                   @Named("godsword_righteous") ItemDefinition godswordRighteous,
                                   @Named("food_fish") ItemDefinition foodFish,
                                   @Named("food_pie") ItemDefinition foodPie,
                                   @Named("food_chicken") ItemDefinition foodChicken,
                                   @Named("food_potato") ItemDefinition foodPotato) {

        this.goblin = goblin;
        this.goblinWarrior = goblinWarrior;
        this.goblinShaman = goblinShaman;
        this.goblinChief = goblinChief;
        this.goldCoin = goldCoin;
        this.steelFullHelm = steelFullHelm;
        this.steelPlatebody = steelPlatebody;
        this.steelPlatelegs = steelPlatelegs;
        this.steelBoots = steelBoots;
        this.steelGloves = steelGloves;
        this.basicScimitar = basicScimitar;
        this.basicSword = basicSword;
        this.godswordEvil = godswordEvil;
        this.godswordRighteous = godswordRighteous;
        this.foodFish = foodFish;
        this.foodPie = foodPie;
        this.foodChicken = foodChicken;
        this.foodPotato = foodPotato;
    }

    @Override
    public Set<NpcDefinition> getNpcs() {
        return Set.of(goblin, goblinWarrior, goblinShaman, goblinChief);
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
