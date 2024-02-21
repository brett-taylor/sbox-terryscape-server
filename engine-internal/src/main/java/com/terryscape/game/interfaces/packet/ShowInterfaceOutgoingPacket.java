package com.terryscape.game.interfaces.packet;

import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;
import java.util.function.Consumer;

public class ShowInterfaceOutgoingPacket implements OutgoingPacket {

    private String interfaceId;

    private Consumer<OutputStream> extraPacketInformation;

    public ShowInterfaceOutgoingPacket setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
        return this;
    }

    public ShowInterfaceOutgoingPacket setExtraPacketInformation(Consumer<OutputStream> extraPacketInformation) {
        this.extraPacketInformation = extraPacketInformation;
        return this;
    }

    @Override
    public String getPacketName() {
        return "server_client_show_interface";
    }

    @Override
    public void writePacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, interfaceId);

        if (extraPacketInformation != null) {
            extraPacketInformation.accept(packet);
        }
    }
}
