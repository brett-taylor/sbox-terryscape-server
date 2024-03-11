package com.terryscape.net;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.entity.Entity;
import com.terryscape.game.player.PlayerComponent;
import com.terryscape.world.WorldManager;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class PacketManagerImpl extends WebSocketServer implements PacketManager {

    private static final Logger LOGGER = LogManager.getLogger(PacketManagerImpl.class);

    private final BidiMap<WebSocket, com.terryscape.net.Client> clients = new DualHashBidiMap<>();

    private final Map<String, IncomingPacket> incomingPacketHandlers = new HashMap<>();

    private final WorldManager worldManager;

    @Inject
    public PacketManagerImpl(Set<IncomingPacket> incomingPackets, WorldManager worldManager) {
        super(new InetSocketAddress(Config.PORT));
        this.worldManager = worldManager;

        setConnectionLostTimeout(Config.WEBSOCKET_PING_TIMEOUT_SECONDS);

        incomingPackets.forEach(incomingPacketHandler -> incomingPacketHandlers.put(incomingPacketHandler.getPacketName(), incomingPacketHandler));
        LOGGER.info("Registered {} incoming packet handlers.", incomingPacketHandlers.size());
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        LOGGER.info("Opened websocket from {}.", conn.getRemoteSocketAddress().getAddress().toString());
        clients.put(conn, new ClientImpl(conn));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        LOGGER.info("Closed websocket code={}, reason={}, remote={}.", code, reason, remote);

        clients.get(conn).getPlayer().ifPresent(playerComponent -> playerComponent.getEntity().delete());

        clients.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        LOGGER.error("Received string websocket");
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        if (!clients.containsKey(conn)) {
            LOGGER.error("Received message from websocket that isn't a registered session.");
            return;
        }

        var client = clients.get(conn);
        var packetName = IncomingPacket.readString(message);

        handleIncomingPacket(client, packetName, message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        LOGGER.error("Websocket error", ex);
    }

    @Override
    public void onStart() {
        LOGGER.info("Launched websocket listener on port {}.", this.getPort());
    }

    @Override
    public void send(com.terryscape.net.Client client, OutgoingPacket outgoingPacket) {
        var packet = generatePacket(outgoingPacket).toByteArray();
        client.getConnection().send(packet);
    }

    @Override
    public void broadcast(OutgoingPacket outgoingPacket) {
        var packet = generatePacket(outgoingPacket).toByteArray();
        broadcast(packet);
    }

    private void handleIncomingPacket(com.terryscape.net.Client client, String packetName, ByteBuffer packet) {
        if (!incomingPacketHandlers.containsKey(packetName)) {
            LOGGER.error("Unhandled packet type {}.", packetName);
            return;
        }

        incomingPacketHandlers.get(packetName).handlePacket(client, packet);
    }

    private ByteArrayOutputStream generatePacket(OutgoingPacket outgoingPacket) {
        var packet = new ByteArrayOutputStream();
        OutgoingPacket.writeString(packet, outgoingPacket.getPacketName());
        outgoingPacket.writePacket(packet);

        return packet;
    }
}
