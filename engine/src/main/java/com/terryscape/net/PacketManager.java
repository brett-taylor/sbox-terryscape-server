package com.terryscape.net;

import java.util.Set;

public interface PacketManager {

    void send(Client client, OutgoingPacket outgoingPacket);

    void broadcast(OutgoingPacket outgoingPacket);

    Set<Client> getClients();
}
