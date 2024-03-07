package com.terryscape.game.player;

import com.google.common.collect.Streams;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class PlayerBonusesProviderComponentImpl extends BaseEntityComponent implements PlayerBonusesProviderComponent {

    private final PlayerComponent playerComponent;

    public PlayerBonusesProviderComponentImpl(Entity entity, PlayerComponent playerComponent) {
        super(entity);

        this.playerComponent = playerComponent;
    }

    @Override
    public String getComponentIdentifier() {
        return "component_player_bonuses_provider";
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        writePacket(packet);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        writePacket(packet);
    }

    @Override
    public float getOffensiveStab() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getOffensiveStab())
            .sum();
    }

    @Override
    public float getOffensiveSlash() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getOffensiveSlash())
            .sum();
    }

    @Override
    public float getDefensiveStab() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getDefensiveStab())
            .sum();
    }

    @Override
    public float getDefensiveSlash() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getDefensiveSlash())
            .sum();
    }

    @Override
    public float getStrengthMelee() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getStrengthMelee())
            .sum();
    }

    @Override
    public float getWeight() {
        return (float) Streams.concat(playerComponent.getInventory().getAllItems().stream(), playerComponent.getEquipment().getAllItems().stream())
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getWeight())
            .sum();
    }

    private void writePacket(OutputStream packet) {
        OutgoingPacket.writeFloat(packet, getWeight());

        OutgoingPacket.writeFloat(packet, getOffensiveStab());
        OutgoingPacket.writeFloat(packet, getOffensiveSlash());

        OutgoingPacket.writeFloat(packet, getDefensiveStab());
        OutgoingPacket.writeFloat(packet, getDefensiveSlash());

        OutgoingPacket.writeFloat(packet, getStrengthMelee());
    }
}
