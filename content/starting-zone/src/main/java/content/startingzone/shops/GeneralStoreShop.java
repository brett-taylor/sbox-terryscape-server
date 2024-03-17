package content.startingzone.shops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopItem;
import jakarta.inject.Named;

import java.util.List;

@Singleton
public class GeneralStoreShop implements Shop {

    private final ItemDefinition hatchet;

    @Inject
    public GeneralStoreShop(@Named("hatchet") ItemDefinition hatchet) {
        this.hatchet = hatchet;
    }

    @Override
    public String getName() {
        return "General Store";
    }

    @Override
    public List<ShopItem> getShopItems() {
        return List.of(
            new ShopItem(hatchet, 75)
        );
    }

}
