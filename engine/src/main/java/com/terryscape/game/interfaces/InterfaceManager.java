package com.terryscape.game.interfaces;

import com.terryscape.net.Client;

import java.io.OutputStream;
import java.util.function.Consumer;

public interface InterfaceManager {

    void showInterface(Client client, String interfaceId);

    void showInterface(Client client, String interfaceId, Consumer<OutputStream> extraInformation);

    void closeInterface(Client client, String interfaceId);
}
