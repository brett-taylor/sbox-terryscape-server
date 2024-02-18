package com.terryscape.game.interfaces.packet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.interfaces.InterfaceActionDispatcher;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;

import java.nio.ByteBuffer;

@Singleton
public class InterfaceActionIncomingPacket implements IncomingPacket {

    private final InterfaceActionDispatcher interfaceActionDispatcher;

    @Inject
    public InterfaceActionIncomingPacket(InterfaceActionDispatcher interfaceActionDispatcher) {
        this.interfaceActionDispatcher = interfaceActionDispatcher;
    }

    @Override
    public String getPacketName() {
        return "client_server_interface_action";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var interfaceId = IncomingPacket.readString(packet);
        var interfaceAction = IncomingPacket.readString(packet);

        interfaceActionDispatcher.dispatchInterfaceAction(client, interfaceId, interfaceAction, packet);
    }
}
