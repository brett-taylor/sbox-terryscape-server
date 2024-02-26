package com.terryscape.game.shop;

import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.task.step.TaskStep;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;

public class ViewShopTaskStep extends TaskStep {

    private final InterfaceManager interfaceManager;

    private final Client client;

    private final Shop shop;

    private boolean isFinished = false;

    public ViewShopTaskStep(InterfaceManager interfaceManager, Shop shop, Client client) {
        this.interfaceManager = interfaceManager;
        this.client = client;
        this.shop = shop;
    }

    @Override
    public void onBecameCurrentTaskStep() {
        super.onBecameCurrentTaskStep();

        interfaceManager.showInterface(client, ShopManagerImpl.SHOP_INTERFACE_ID,  stream -> {
            OutgoingPacket.writeString(stream, shop.getName());

            OutgoingPacket.writeInt32(stream, shop.getShopItems().size());

            shop.getShopItems().forEach(shopItem -> {
                OutgoingPacket.writeString(stream, shopItem.getItemDefinition().getId());
                OutgoingPacket.writeInt32(stream, shopItem.getPrice());
            });
        });
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void cancel() {
        super.cancel();

        interfaceManager.closeInterface(client, ShopManagerImpl.SHOP_INTERFACE_ID);
    }

    public Shop getShop() {
        return shop;
    }

    public void closeShop() {
        if (isFinished) {
            return;
        }

        interfaceManager.closeInterface(client, ShopManagerImpl.SHOP_INTERFACE_ID);
        isFinished = true;
    }
}
