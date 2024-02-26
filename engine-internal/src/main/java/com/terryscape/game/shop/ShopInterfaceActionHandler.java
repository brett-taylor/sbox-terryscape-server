package com.terryscape.game.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class ShopInterfaceActionHandler implements InterfaceActionHandler {

    private final InterfaceManager interfaceManager;

    @Inject
    public ShopInterfaceActionHandler(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
    }

    @Override
    public Set<String> getInterfaceId() {
        return Set.of(ShopManagerImpl.SHOP_INTERFACE_ID);
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        interfaceManager.closeInterface(client, ShopManagerImpl.SHOP_INTERFACE_ID);
    }
}
