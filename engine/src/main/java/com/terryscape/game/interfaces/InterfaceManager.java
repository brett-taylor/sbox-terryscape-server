package com.terryscape.game.interfaces;

import com.terryscape.net.Client;

public interface InterfaceManager {

    void showInterface(Client client, String interfaceId);

    void closeInterface(Client client, String interfaceId);
}
