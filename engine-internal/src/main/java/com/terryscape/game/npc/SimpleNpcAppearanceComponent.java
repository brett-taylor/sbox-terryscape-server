package com.terryscape.game.npc;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class SimpleNpcAppearanceComponent extends BaseEntityComponent implements NetworkedEntityComponent {

    private String variant;

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    @Override
    public String getComponentIdentifier() {
        return "component_simple_npc_appearance";
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, getVariant());
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {

    }

}
