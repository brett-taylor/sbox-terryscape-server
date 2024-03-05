package com.terryscape.game.player;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class PlayerSkillsComponentImpl extends BaseEntityComponent implements PlayerSkillsComponent {

    public PlayerSkillsComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_player_skills";
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
    public int getAttack() {
        return 40;
    }

    @Override
    public int getDefence() {
        return 40;
    }

    @Override
    public int getStrength() {
        return 40;
    }

    private void writePacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, getAttack());
        OutgoingPacket.writeInt32(packet, getDefence());
        OutgoingPacket.writeInt32(packet, getStrength());
    }
}
