package content.startingzone.shops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopItem;

import java.util.List;

@Singleton
public class FoodShop implements Shop {

    private final ItemDefinition foodCheese;

    private final ItemDefinition foodCheeseWheel;

    private final ItemDefinition foodFish;

    private final ItemDefinition foodChicken;

    private final ItemDefinition foodRawChicken;

    private final ItemDefinition foodPie;

    private final ItemDefinition foodPotato;

    @Inject
    public FoodShop(@Named("food_cheese") ItemDefinition foodCheese,
                    @Named("food_cheese_wheel") ItemDefinition foodCheeseWheel,
                    @Named("food_fish") ItemDefinition foodFish,
                    @Named("food_chicken") ItemDefinition foodChicken,
                    @Named("food_raw_chicken") ItemDefinition foodRawChicken,
                    @Named("food_pie") ItemDefinition foodPie,
                    @Named("food_potato") ItemDefinition foodPotato) {

        this.foodCheese = foodCheese;
        this.foodCheeseWheel = foodCheeseWheel;
        this.foodFish = foodFish;
        this.foodChicken = foodChicken;
        this.foodRawChicken = foodRawChicken;
        this.foodPie = foodPie;
        this.foodPotato = foodPotato;
    }

    @Override
    public String getName() {
        return "The Food Stall";
    }

    @Override
    public List<ShopItem> getShopItems() {
        return List.of(
            new ShopItem(foodCheese, 10),
            new ShopItem(foodCheeseWheel, 1),
            new ShopItem(foodFish, 50),
            new ShopItem(foodChicken, 25),
            new ShopItem(foodRawChicken, 1),
            new ShopItem(foodPie, 100),
            new ShopItem(foodPotato, 5)
        );
    }
}
