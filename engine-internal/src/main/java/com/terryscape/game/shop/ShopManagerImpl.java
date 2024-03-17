package com.terryscape.game.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.task.step.TaskStep;
import com.terryscape.net.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class ShopManagerImpl implements ShopManager {

    public static String SHOP_INTERFACE_ID = "shop";

    private static final Logger LOGGER = LogManager.getLogger(ShopManagerImpl.class);

    private final InterfaceManager interfaceManager;

    private final SoundManager soundManager;

    private final ItemDefinition goldCoinItemDefinition;

    private final SoundDefinition failedSoundDefinition;

    private final SoundDefinition purchasedSoundDefinition;

    @Inject
    public ShopManagerImpl(InterfaceManager interfaceManager, SoundManager soundManager,
                           @Named("gold_coin") ItemDefinition goldCoinItemDefinition,
                           @Named("shop_purchased") SoundDefinition failedSoundDefinition,
                           @Named("shop_failed") SoundDefinition purchasedSoundDefinition) {

        this.interfaceManager = interfaceManager;
        this.soundManager = soundManager;
        this.goldCoinItemDefinition = goldCoinItemDefinition;
        this.failedSoundDefinition = failedSoundDefinition;
        this.purchasedSoundDefinition = purchasedSoundDefinition;
    }

    @Override
    public TaskStep createViewShopTaskStep(Shop shop, Client client) {
        return new ViewShopTaskStep(interfaceManager, shop, client);
    }

    public void buyItemFromShop(Shop shop, PlayerComponent playerComponent, ItemDefinition itemDefinition, int quantity) {
        if (!isItemInStockInShop(shop, itemDefinition)) {
            LOGGER.error("{} attempted to buy item {} from {} which it does not stock", playerComponent.getUsername(), itemDefinition, shop.getName());
            return;
        }

        var playerInventory = playerComponent.getInventory();
        var playerChat = playerComponent.getEntity().getComponentOrThrow(PlayerChatComponent.class);

        var price = getPriceForItemInShop(shop, itemDefinition);
        var totalPrice = price * quantity;

        // Check player inventory has n slots
        if (!playerInventory.hasFreeSlots(quantity)) {
            playerChat.sendGameMessage("You do not have the space in your inventory to buy %d %s.".formatted(quantity, itemDefinition.getName()));
            return;
        }

        if (playerInventory.removeItemOfTypeAndQuantity(goldCoinItemDefinition, totalPrice)) {
            playerInventory.addItem(itemDefinition, quantity);
            playerChat.sendGameMessage("You bought %d %s.".formatted(quantity, itemDefinition.getName()));
            soundManager.playSoundEffect(playerComponent.getClient(), purchasedSoundDefinition);
        } else {
            soundManager.playSoundEffect(playerComponent.getClient(), failedSoundDefinition);
            playerChat.sendGameMessage("You do not have the required %d %s.".formatted(totalPrice, goldCoinItemDefinition.getName()));
        }
    }

    private boolean isItemInStockInShop(Shop shop, ItemDefinition itemDefinition) {
        return shop.getShopItems().stream().anyMatch(shopItem -> shopItem.getItemDefinition() == itemDefinition);
    }

    private int getPriceForItemInShop(Shop shop, ItemDefinition itemDefinition) {
        return shop.getShopItems().stream()
            .filter(shopItem -> shopItem.getItemDefinition() == itemDefinition)
            .findFirst()
            .map(ShopItem::getPrice)
            .orElseThrow();
    }
}
