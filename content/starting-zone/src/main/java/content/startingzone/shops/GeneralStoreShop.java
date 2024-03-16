package content.startingzone.shops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopItem;

import java.util.List;

@Singleton
public class GeneralStoreShop implements Shop {

    private final CacheLoader cacheLoader;

    @Inject
    public GeneralStoreShop(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getName() {
        return "General Store";
    }

    @Override
    public List<ShopItem> getShopItems() {
        return List.of(
            new ShopItem(cacheLoader.getItemDefinition("hatchet"), 75)
        );
    }

}
