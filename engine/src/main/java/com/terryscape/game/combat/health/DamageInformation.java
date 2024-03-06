package com.terryscape.game.combat.health;

import com.terryscape.game.combat.DamageType;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketSerializable;

import java.io.OutputStream;

public class DamageInformation implements PacketSerializable {

    private int amount;

    private DamageType type;

    public DamageInformation() {
        amount = 0;
        type = DamageType.TYPELESS;
    }

    public int getAmount() {
        return amount;
    }

    public DamageInformation setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public DamageType getType() {
        return type;
    }

    public DamageInformation setDamageType(DamageType type) {
        this.type = type;
        return this;
    }

    @Override
    public void writeToPacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, getAmount());
        OutgoingPacket.writeEnum(packet, getType());
    }
}
