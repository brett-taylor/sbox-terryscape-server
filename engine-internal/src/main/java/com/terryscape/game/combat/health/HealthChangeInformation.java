package com.terryscape.game.combat.health;

import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketSerializable;

import java.io.OutputStream;

public class HealthChangeInformation implements PacketSerializable {

    private HealthChangeInformationType healthChangeInformationType;

    private int amount;

    public HealthChangeInformationType getHealthChangeType() {
        return healthChangeInformationType;
    }

    public HealthChangeInformation setHealthChangeType(HealthChangeInformationType healthChangeInformationType) {
        this.healthChangeInformationType = healthChangeInformationType;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public HealthChangeInformation setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public void writeToPacket(OutputStream packet) {
        OutgoingPacket.writeEnum(packet, getHealthChangeType());
        OutgoingPacket.writeInt32(packet, getAmount());
    }
}
