package com.terryscape.game.npc;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class NpcComponentImpl extends BaseEntityComponent implements NpcComponent {

    private NpcDefinition npcDefinition;

    private String npcVariant;

    public NpcComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_npc_component";
    }

    @Override
    public NpcDefinition getNpcDefinition() {
        return npcDefinition;
    }

    public void setNpcDefinition(NpcDefinition npcDefinition) {
        this.npcDefinition = npcDefinition;
    }

    @Override
    public String getNpcVariant() {
        return npcVariant;
    }

    public void setNpcVariant(String npcVariant) {
        this.npcVariant = npcVariant;
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, npcDefinition.getId());
        OutgoingPacket.writeString(packet, getNpcVariant());
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
    }

}
