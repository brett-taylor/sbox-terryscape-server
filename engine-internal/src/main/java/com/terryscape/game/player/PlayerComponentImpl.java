package com.terryscape.game.player;

import com.google.inject.Inject;
import com.terryscape.Config;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.equipment.PlayerEquipment;
import com.terryscape.game.equipment.PlayerEquipmentImpl;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.game.item.PlayerInventory;
import com.terryscape.game.login.SetLocalPlayerOutgoingPacket;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WaitStep;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketManager;
import com.terryscape.world.Direction;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.io.OutputStream;

public class PlayerComponentImpl extends BaseEntityComponent implements PlayerComponent {

    private final PacketManager packetManager;

    private Client client;

    private String username;

    private FixedSizeItemContainer inventory;

    private PlayerEquipment equipment;

    private PlayerGender playerGender;

    @Inject
    public PlayerComponentImpl(Entity entity, PacketManager packetManager) {
        super(entity);

        this.packetManager = packetManager;

        inventory = new PlayerInventory();
        equipment = new PlayerEquipmentImpl();

        getEntity().subscribe(OnEntityDeathEntityEvent.class, this::onDeath);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_player_component";
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
    public void setGender(PlayerGender gender) {
        playerGender = gender;
    }

    @Override
    public PlayerGender getGender() {
        return playerGender;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        respawn();

        getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage("Welcome to %s, %s.".formatted(Config.NAME, getUsername()));
        getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage("Say ::help to see commands.");

        var setLocalEntityPacket = new SetLocalPlayerOutgoingPacket().setLocalEntity(this);
        packetManager.send(getClient(), setLocalEntityPacket);
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, getUsername());
        getInventory().writeToPacket(packet);
        getEquipment().writeToPacket(packet);
        OutgoingPacket.writeEnum(packet, getGender());
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        getInventory().writeToPacket(packet);
        getEquipment().writeToPacket(packet);
        OutgoingPacket.writeEnum(packet, getGender());
    }

    private void onDeath(OnEntityDeathEntityEvent onEntityDeathEntityEvent) {
        getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage("You died!");
        getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation("death");

        getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
            WaitStep.ticks(8),
            ImmediateStep.run(this::respawn)
        );
    }

    private void respawn() {
        getEntity().getComponentOrThrow(AnimationComponent.class).resetAnimation();
        getEntity().getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(15, 15));
        getEntity().getComponentOrThrow(MovementComponent.class).look(Direction.NORTH);

        getEntity().getComponentOrThrow(HealthComponent.class).resetHealthToMax();
    }
}
