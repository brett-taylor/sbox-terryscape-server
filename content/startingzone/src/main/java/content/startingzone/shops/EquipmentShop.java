package content.startingzone.shops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopItem;

import java.util.List;

@Singleton
public class EquipmentShop implements Shop {

    private final CacheLoader cacheLoader;

    @Inject
    public EquipmentShop(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getName() {
        return "The Quality Armour Shop";
    }

    @Override
    public List<ShopItem> getShopItems() {
        return List.of(
            new ShopItem(cacheLoader.getItemDefinition("wizard_hat"), 20),
            new ShopItem(cacheLoader.getItemDefinition("wizard_top"), 50),
            new ShopItem(cacheLoader.getItemDefinition("wizard_bottoms"), 50),
            new ShopItem(cacheLoader.getItemDefinition("wizard_gloves"), 10),
            new ShopItem(cacheLoader.getItemDefinition("wizard_boots"), 10),

            new ShopItem(cacheLoader.getItemDefinition("steel_full_helm"), 200),
            new ShopItem(cacheLoader.getItemDefinition("steel_platebody"), 500),
            new ShopItem(cacheLoader.getItemDefinition("steel_platelegs"), 500),
            new ShopItem(cacheLoader.getItemDefinition("steel_gloves"), 100),
            new ShopItem(cacheLoader.getItemDefinition("steel_boots"), 100)
        );
    }
}
