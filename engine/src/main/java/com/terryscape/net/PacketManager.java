package com.terryscape.net;

import com.terryscape.net.packet.OutgoingPacket;

public interface PacketManager {

    void send(Client client, OutgoingPacket outgoingPacket);

    void broadcast(OutgoingPacket outgoingPacket);
}
