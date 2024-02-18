package com.terryscape.game.interfaces;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.game.interfaces.packet.CloseInterfaceOutgoingPacket;
import com.terryscape.game.interfaces.packet.ShowInterfaceOutgoingPacket;
import com.terryscape.net.Client;
import com.terryscape.net.PacketManager;

@Singleton
public class InterfaceManagerImpl implements InterfaceManager {

    private final PacketManager packetManager;

    @Inject
    public InterfaceManagerImpl(PacketManager packetManager) {
        this.packetManager = packetManager;
    }

    @Override
    public void showInterface(Client client, String interfaceId) {
        var packet = new ShowInterfaceOutgoingPacket().setInterfaceId(interfaceId);
        packetManager.send(client, packet);
    }

    @Override
    public void closeInterface(Client client, String interfaceId) {
        var packet = new CloseInterfaceOutgoingPacket().setInterfaceId(interfaceId);
        packetManager.send(client, packet);
    }
}
