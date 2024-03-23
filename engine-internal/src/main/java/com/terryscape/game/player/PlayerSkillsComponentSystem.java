package com.terryscape.game.player;

import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

@Singleton
public class PlayerSkillsComponentSystem extends ComponentSystem<PlayerSkillsComponentImpl> {

    @Override
    public Class<PlayerSkillsComponentImpl> forComponentType() {
        return PlayerSkillsComponentImpl.class;
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_player_skills";
    }

    @Override
    public void writeEntityAddedPacket(Entity entity, PlayerSkillsComponentImpl component, OutputStream packet) {
        writePacket(component, packet);
    }

    @Override
    public void writeEntityUpdatedPacket(Entity entity, PlayerSkillsComponentImpl component, OutputStream packet) {
        writePacket(component, packet);
    }

    private void writePacket(PlayerSkillsComponentImpl component, OutputStream packet) {
        OutgoingPacket.writeInt32(packet, component.getCombat());
        OutgoingPacket.writeInt32(packet, component.getAttack());
        OutgoingPacket.writeInt32(packet, component.getDefence());
        OutgoingPacket.writeInt32(packet, component.getStrength());
        OutgoingPacket.writeInt32(packet, component.getMagic());
        OutgoingPacket.writeInt32(packet, component.getRange());
        OutgoingPacket.writeInt32(packet, component.getConstitution());
    }
}
