package com.terryscape.game.interfaces.packet;

import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class CloseInterfaceOutgoingPacket implements OutgoingPacket {

    private String interfaceId;

    public CloseInterfaceOutgoingPacket setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
        return this;
    }

    @Override
    public String getPacketName() {
        return "server_client_close_interface";
    }

    @Override
    public void writePacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, interfaceId);
    }
}
