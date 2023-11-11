package com.terryscape.game.player;

import com.google.inject.Inject;
import com.terryscape.Config;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.equipment.PlayerEquipment;
import com.terryscape.game.equipment.PlayerEquipmentImpl;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.game.item.PlayerInventory;
import com.terryscape.game.login.SetLocalPlayerOutgoingPacket;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketManager;

import java.io.OutputStream;

public class PlayerComponentImpl extends BaseEntityComponent implements PlayerComponent {

    private final PacketManager packetManager;

    private final CacheLoader cacheLoader;

    private Client client;

    private String username;

    private FixedSizeItemContainer inventory;

    private PlayerEquipment equipment;

    @Inject
    public PlayerComponentImpl(Entity entity, PacketManager packetManager, CacheLoader cacheLoader) {
        super(entity);

        this.packetManager = packetManager;
        this.cacheLoader = cacheLoader;
    }

    @Override
    public String getComponentIdentifier() {
        return "component_player_component";
    }

    @Override
    public void onAdded() {
        super.onAdded();

        inventory = new PlayerInventory();
        equipment = new PlayerEquipmentImpl();
    }

    @Override
    public Client getClient() {
        return client;
    }

    public PlayerComponentImpl setClient(Client client) {
        this.client = client;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public PlayerComponentImpl setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public FixedSizeItemContainer getInventory() {
        return inventory;
    }

    @Override
    public PlayerEquipment getEquipment() {
        return equipment;
    }

    @Override
    public void onRegister() {
        super.onRegister();

        getInventory().addItem(cacheLoader.getItem("wooden_longsword"));
        getInventory().addItem(cacheLoader.getItem("wooden_scimitar"));
    }

    @Override
    public void onSpawn() {
        super.onSpawn();

        var setLocalEntityPacket = new SetLocalPlayerOutgoingPacket().setLocalEntity(this);
        packetManager.send(getClient(), setLocalEntityPacket);

        var welcomeMessage = "Welcome to %s, %s.".formatted(Config.NAME, getUsername());
        getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(welcomeMessage);
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, getUsername());
        getInventory().writeToPacket(packet);
        getEquipment().writeToPacket(packet);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        getInventory().writeToPacket(packet);
        getEquipment().writeToPacket(packet);
    }
}
