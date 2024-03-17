package content.startingzone.shops;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.game.shop.Shop;
import com.terryscape.game.shop.ShopItem;

import java.util.List;

@Singleton
public class WeaponShop implements Shop {

    private final ItemDefinition basicScimitar;

    private final ItemDefinition basicSword;

    private final ItemDefinition godswordRighteous;

    private final ItemDefinition godswordEvil;

    private final ItemDefinition basicAirStaff;

    private final ItemDefinition basicFireStaff;

    private final ItemDefinition basicBow;

    @Inject
    public WeaponShop(@Named("basic_scimitar") ItemDefinition basicScimitar,
                      @Named("basic_sword") ItemDefinition basicSword,
                      @Named("godsword_righteous") ItemDefinition godswordRighteous,
                      @Named("godsword_evil") ItemDefinition godswordEvil,
                      @Named("basic_air_staff") ItemDefinition basicAirStaff,
                      @Named("basic_fire_staff") ItemDefinition basicFireStaff,
                      @Named("basic_bow") ItemDefinition basicBow) {

        this.basicScimitar = basicScimitar;
        this.basicSword = basicSword;
        this.godswordRighteous = godswordRighteous;
        this.godswordEvil = godswordEvil;
        this.basicAirStaff = basicAirStaff;
        this.basicFireStaff = basicFireStaff;
        this.basicBow = basicBow;
    }

    @Override
    public String getName() {
        return "Sharp Arms";
    }

    @Override
    public List<ShopItem> getShopItems() {
        return List.of(
            new ShopItem(basicScimitar, 200),
            new ShopItem(basicSword, 200),
            new ShopItem(godswordRighteous, 4000),
            new ShopItem(godswordEvil, 4000),
            new ShopItem(basicAirStaff, 200),
            new ShopItem(basicFireStaff, 200),
            new ShopItem(basicBow, 200)
        );
    }

}
