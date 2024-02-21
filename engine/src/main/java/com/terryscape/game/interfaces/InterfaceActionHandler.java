package com.terryscape.game.interfaces;

import com.terryscape.net.Client;

import java.nio.ByteBuffer;
import java.util.Set;

public interface InterfaceActionHandler {

    Set<String> getInterfaceId();

    void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet);
}
