package com.terryscape.net;

public interface PacketManager {

    void send(Client client, OutgoingPacket outgoingPacket);

    void broadcast(OutgoingPacket outgoingPacket);
}
