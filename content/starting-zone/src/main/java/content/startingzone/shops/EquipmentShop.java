package content.startingzone.shops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopItem;

import java.util.List;

@Singleton
public class EquipmentShop implements Shop {

    private final ItemDefinition wizardHat;

    private final ItemDefinition wizardTop;

    private final ItemDefinition wizardBottoms;

    private final ItemDefinition wizardGloves;

    private final ItemDefinition wizardBoots;

    private final ItemDefinition steelFullHelm;

    private final ItemDefinition steelPlatebody;

    private final ItemDefinition steelPlatelegs;

    private final ItemDefinition steelGloves;

    private final ItemDefinition steelBoots;

    @Inject
    public EquipmentShop(@Named("wizard_hat") ItemDefinition wizardHat,
                         @Named("wizard_top") ItemDefinition wizardTop,
                         @Named("wizard_bottoms") ItemDefinition wizardBottoms,
                         @Named("wizard_gloves") ItemDefinition wizardGloves,
                         @Named("wizard_boots") ItemDefinition wizardBoots,
                         @Named("steel_full_helm") ItemDefinition steelFullHelm,
                         @Named("steel_platebody") ItemDefinition steelPlatebody,
                         @Named("steel_platelegs") ItemDefinition steelPlatelegs,
                         @Named("steel_gloves") ItemDefinition steelGloves,
                         @Named("steel_boots") ItemDefinition steelBoots) {

        this.wizardHat = wizardHat;
        this.wizardTop = wizardTop;
        this.wizardBottoms = wizardBottoms;
        this.wizardGloves = wizardGloves;
        this.wizardBoots = wizardBoots;
        this.steelFullHelm = steelFullHelm;
        this.steelPlatebody = steelPlatebody;
        this.steelPlatelegs = steelPlatelegs;
        this.steelGloves = steelGloves;
        this.steelBoots = steelBoots;
    }

    @Override
    public String getName() {
        return "The Quality Armour Shop";
    }

    @Override
    public List<ShopItem> getShopItems() {
        return List.of(
            new ShopItem(wizardHat, 20),
            new ShopItem(wizardTop, 50),
            new ShopItem(wizardBottoms, 50),
            new ShopItem(wizardGloves, 10),
            new ShopItem(wizardBoots, 10),

            new ShopItem(steelFullHelm, 200),
            new ShopItem(steelPlatebody, 500),
            new ShopItem(steelPlatelegs, 500),
            new ShopItem(steelGloves, 100),
            new ShopItem(steelBoots, 100)
        );
    }
}
