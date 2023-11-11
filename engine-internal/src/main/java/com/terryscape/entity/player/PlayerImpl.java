package com.terryscape.entity.player;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.terryscape.Config;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityImpl;
import com.terryscape.entity.EntityType;
import com.terryscape.net.Client;
import com.terryscape.net.PacketManager;
import com.terryscape.net.packet.OutgoingPacket;
import com.terryscape.net.packet.outgoing.SetLocalPlayerOutgoingPacket;
import com.terryscape.system.chat.PlayerChatImpl;
import com.terryscape.system.equipment.PlayerEquipment;
import com.terryscape.system.equipment.PlayerEquipmentImpl;
import com.terryscape.system.item.PlayerInventory;
import com.terryscape.system.movement.PlayerMovementImpl;
import com.terryscape.world.Direction;
import com.terryscape.world.WorldCoordinate;

import java.io.OutputStream;

public class PlayerImpl extends EntityImpl implements Player {

    public interface PlayerImplFactory {
        PlayerImpl create(Client client);
    }

    private final PacketManager packetManager;

    private final CacheLoader cacheLoader;

    private final PlayerChatImpl playerChat;

    private final PlayerMovementImpl playerMovement;

    private final PlayerInventory playerInventory;

    private final PlayerEquipmentImpl playerEquipment;

    private final Client client;

    private String username;

    @Inject
    public PlayerImpl(PacketManager packetManager,
                      CacheLoader cacheLoader,
                      PlayerChatImpl.PlayerChatImplFactory playerChatImplFactory,
                      PlayerMovementImpl.PlayerMovementImplFactory playerMovementFactory,
                      @Assisted Client client) {

        this.packetManager = packetManager;
        this.cacheLoader = cacheLoader;
        this.client = client;

        this.playerMovement = playerMovementFactory.create();
        this.playerChat = playerChatImplFactory.create(this);
        this.playerInventory = new PlayerInventory();
        this.playerEquipment = new PlayerEquipmentImpl();
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLAYER;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void register() {
        super.register();

        getPlayerMovement().teleport(new WorldCoordinate(5, 0));
        getPlayerMovement().look(Direction.NORTH_EAST);
    }

    @Override
    public void spawn() {
        super.spawn();

        var setLocalEntityPacket = new SetLocalPlayerOutgoingPacket().setLocalEntity(this);
        packetManager.send(getClient(), setLocalEntityPacket);

        var welcomeMessage = "Welcome to %s, %s.".formatted(Config.NAME, getUsername());
        getPlayerChat().sendGameMessage(welcomeMessage);

        getPlayerInventory().addItem(cacheLoader.getItem("wooden_longsword"));
        getPlayerInventory().addItem(cacheLoader.getItem("wooden_scimitar"));
    }

    @Override
    public void tick() {
        super.tick();

        getPlayerMovement().tick();
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        super.writeEntityAddedPacket(packet);

        OutgoingPacket.writeString(packet, username);

        getPlayerMovement().writeEntityAddedPacket(packet);
        getPlayerInventory().writeToPacket(packet);
        getPlayerEquipment().writeToPacket(packet);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        super.writeEntityUpdatedPacket(packet);

        getPlayerMovement().writeEntityUpdatedPacket(packet);
        getPlayerInventory().writeToPacket(packet);
        getPlayerEquipment().writeToPacket(packet);
    }

    @Override
    public PlayerChatImpl getPlayerChat() {
        return playerChat;
    }

    @Override
    public PlayerMovementImpl getPlayerMovement() {
        return playerMovement;
    }

    @Override
    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    @Override
    public PlayerEquipment getPlayerEquipment() {
        return playerEquipment;
    }
}
