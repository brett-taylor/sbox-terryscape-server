package com.terryscape.game.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;

@Singleton
public class ShopManagerImpl implements ShopManager {

    public static String SHOP_INTERFACE_ID = "shop";

    private final InterfaceManager interfaceManager;

    @Inject
    public ShopManagerImpl(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
    }

    @Override
    public void showShop(Client client, Shop shop) {
        interfaceManager.showInterface(client, SHOP_INTERFACE_ID,  stream -> {
            OutgoingPacket.writeString(stream, shop.getName());

            OutgoingPacket.writeInt32(stream, shop.getShopItems().size());

            shop.getShopItems().forEach(shopItem -> {
                OutgoingPacket.writeString(stream, shopItem.getItemDefinition().getId());
                OutgoingPacket.writeInt32(stream, shopItem.getPrice());
            });
        });
    }
}
