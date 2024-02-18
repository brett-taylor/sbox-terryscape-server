package com.terryscape.game.interfaces;

import com.terryscape.net.Client;

import java.nio.ByteBuffer;

public interface InterfaceActionHandler {

    String getInterfaceId();

    void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet);
}
