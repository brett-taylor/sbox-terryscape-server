package com.terryscape.net;

import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface PacketSerializable {

    void writeToPacket(OutputStream packet);

}
