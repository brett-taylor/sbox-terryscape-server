package content.startingzone.shops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopItem;

import java.util.List;

@Singleton
public class WeaponShop implements Shop {

    private final CacheLoader cacheLoader;

    @Inject
    public WeaponShop(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getName() {
        return "Sharp Arms";
    }

    @Override
    public List<ShopItem> getShopItems() {
        return List.of(
            new ShopItem(cacheLoader.getItem("basic_scimitar"), 250),
            new ShopItem(cacheLoader.getItem("basic_sword"), 150),
            new ShopItem(cacheLoader.getItem("godsword_righteous"), 4000),
            new ShopItem(cacheLoader.getItem("godsword_evil"), 4000)
        );
    }

}
