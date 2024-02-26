package com.terryscape.game.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class ShopInterfaceActionHandler implements InterfaceActionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ShopManagerImpl.class);

    private final CacheLoader cacheLoader;

    private final ShopManagerImpl shopManager;

    @Inject
    public ShopInterfaceActionHandler(CacheLoader cacheLoader, ShopManagerImpl shopManager) {
        this.cacheLoader = cacheLoader;
        this.shopManager = shopManager;
    }

    @Override
    public Set<String> getInterfaceId() {
        return Set.of(ShopManagerImpl.SHOP_INTERFACE_ID);
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var playerComponent = client.getPlayer().orElseThrow();
        var taskComponent = playerComponent.getEntity().getComponentOrThrow(TaskComponent.class);

        if (!taskComponent.hasPrimaryTask()) {
            LOGGER.warn(
                "{} triggered the shop interface action handler without a primary task",
                client.getPlayer().map(PlayerComponent::getUsername).orElse("")
            );
            return;
        }

        var currentTaskStep = taskComponent.getPrimaryTask().orElseThrow().getCurrentTaskStep();
        if (!(currentTaskStep instanceof ViewShopTaskStep viewShopTaskStep)) {
            LOGGER.warn(
                "{} triggered the shop interface action handler without their primary task being ViewShop",
                client.getPlayer().map(PlayerComponent::getUsername).orElse("")
            );

            return;
        }

        if (interfaceAction.equals("close")) {
            viewShopTaskStep.closeShop();
        }

        if (interfaceAction.equals("buy")) {
            var item = cacheLoader.getItem(IncomingPacket.readString(packet));
            var quantity = IncomingPacket.readInt32(packet);
            assert quantity > 0 && quantity < 21;

            shopManager.buyItemFromShop(viewShopTaskStep.getShop(), playerComponent, item, quantity);
        }
    }
}
