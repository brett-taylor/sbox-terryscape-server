package com.terryscape.game.shop;

import com.terryscape.game.task.step.TaskStep;
import com.terryscape.net.Client;

public interface ShopManager {

    TaskStep createViewShopTaskStep(Shop shop, Client client);

}
