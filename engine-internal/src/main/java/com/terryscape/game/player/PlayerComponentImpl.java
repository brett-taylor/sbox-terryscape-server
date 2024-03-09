package com.terryscape.game.player;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.game.appearance.HumanoidGender;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.equipment.PlayerEquipment;
import com.terryscape.game.equipment.PlayerEquipmentImpl;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.game.item.PlayerInventory;
import com.terryscape.game.login.SetLocalPlayerOutgoingPacket;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.NextTickTaskStep;
import com.terryscape.game.task.step.impl.WaitTaskStep;
import com.terryscape.net.Client;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketManager;
import com.terryscape.world.Direction;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.io.OutputStream;

public class PlayerComponentImpl extends BaseEntityComponent implements PlayerComponent {

    private final PacketManager packetManager;

    private final InterfaceManager interfaceManager;

    private final FixedSizeItemContainer inventory;

    private final PlayerEquipment equipment;

    private Client client;

    private String username;

    private HumanoidGender gender;

    private float specialAttackPower;

    private boolean wantsToSpecialAttack;

    public PlayerComponentImpl(Entity entity, PacketManager packetManager, InterfaceManager interfaceManager) {
        super(entity);

        this.packetManager = packetManager;
        this.interfaceManager = interfaceManager;

        inventory = new PlayerInventory();
        equipment = new PlayerEquipmentImpl();

        specialAttackPower = 100f;

        getEntity().subscribe(OnEntityDeathEntityEvent.class, this::onDeath);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_player";
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
    public void setGender(HumanoidGender gender) {
        this.gender = gender;
    }

    @Override
    public HumanoidGender getGender() {
        return gender;
    }

    @Override
    public float getSpecialAttackPower() {
        return specialAttackPower;
    }

    @Override
    public void setSpecialAttackPower(float specialAttackPower) {
        this.specialAttackPower = specialAttackPower;
    }

    @Override
    public boolean wantsToSpecialAttack() {
        return wantsToSpecialAttack;
    }

    @Override
    public void setWantsToSpecialAttack(boolean wantsToSpecialAttack) {
        this.wantsToSpecialAttack = wantsToSpecialAttack;
    }

    @Override
    public void onRegistered() {
        super.onRegistered();

        respawn();

        interfaceManager.showInterface(getClient(), "welcome_screen");

        var setLocalEntityPacket = new SetLocalPlayerOutgoingPacket().setLocalEntity(this);
        packetManager.send(getClient(), setLocalEntityPacket);
    }

    @Override
    public void tick() {
        super.tick();

        if (specialAttackPower < 100) {
            specialAttackPower += 0.5f;
        }
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, getUsername());
        getInventory().writeToPacket(packet);
        getEquipment().writeToPacket(packet);
        OutgoingPacket.writeEnum(packet, getGender());
        OutgoingPacket.writeFloat(packet, getSpecialAttackPower());
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        getInventory().writeToPacket(packet);
        getEquipment().writeToPacket(packet);
        OutgoingPacket.writeEnum(packet, getGender());
        OutgoingPacket.writeFloat(packet, getSpecialAttackPower());
        OutgoingPacket.writeBoolean(packet, wantsToSpecialAttack());
    }

    private void onDeath(OnEntityDeathEntityEvent onEntityDeathEntityEvent) {
        getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage("You died!");
        getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation("death");

        getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
            WaitTaskStep.ticks(8),
            NextTickTaskStep.doThis(this::respawn)
        );
    }

    private void respawn() {
        getEntity().getComponentOrThrow(AnimationComponent.class).resetAnimation();
        getEntity().getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(14, 20));
        getEntity().getComponentOrThrow(MovementComponent.class).look(Direction.SOUTH);

        getEntity().getComponentOrThrow(HealthComponent.class).resetHealthToMax();
    }
}
