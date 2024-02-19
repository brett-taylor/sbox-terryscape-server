package com.terryscape.game.combat.health;

import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketSerializable;

import java.io.OutputStream;

public class DamageInformation implements PacketSerializable {
    private boolean hit;
    private boolean mainHand;
    private int amount;
    private DamageType type;

    public DamageInformation() {
        hit = true;
        mainHand = true;
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

    public DamageInformation setType(DamageType type) {
        this.type = type;
        return this;
    }

    public DamageInformation setHit(boolean hit){
        this.hit = hit;
        return this;
    }

    @Override
    public void writeToPacket(OutputStream packet) {
        OutgoingPacket.writeBoolean(packet, getHit());
        OutgoingPacket.writeBoolean(packet, getIsUsingMainHand());
        OutgoingPacket.writeInt32(packet, getAmount());
        OutgoingPacket.writeEnum(packet, getType());
    }

    public DamageInformation setIsUsingMainHand(boolean mainHand){
        this.mainHand = mainHand;
        return this;
    }

    private boolean getIsUsingMainHand() {
        return mainHand;
    }

    private boolean getHit() {
        return hit;
    }
}
