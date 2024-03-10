package content.startingzone.shops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopItem;

import java.util.List;

@Singleton
public class FoodShop implements Shop {

    private final CacheLoader cacheLoader;

    @Inject
    public FoodShop(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getName() {
        return "The Food Stall";
    }

    @Override
    public List<ShopItem> getShopItems() {
        return List.of(
            new ShopItem(cacheLoader.getItemDefinition("food_cheese"), 10),
            new ShopItem(cacheLoader.getItemDefinition("food_cheese_wheel"), 1),
            new ShopItem(cacheLoader.getItemDefinition("food_fish"), 50),
            new ShopItem(cacheLoader.getItemDefinition("food_chicken"), 25),
            new ShopItem(cacheLoader.getItemDefinition("food_raw_chicken"), 1),
            new ShopItem(cacheLoader.getItemDefinition("food_pie"), 100),
            new ShopItem(cacheLoader.getItemDefinition("food_potato"), 5)
        );
    }
}
