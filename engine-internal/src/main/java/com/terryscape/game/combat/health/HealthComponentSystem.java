package com.terryscape.game.combat.health;

import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

@Singleton
public class HealthComponentSystem extends ComponentSystem<HealthComponentImpl> {

    @Override
    public Class<HealthComponentImpl> forComponentType() {
        return HealthComponentImpl.class;
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_health";
    }

    @Override
    public void writeEntityAddedPacket(Entity entity, HealthComponentImpl component, OutputStream packet) {
        OutgoingPacket.writeInt32(packet, component.getMaxHealth());
        OutgoingPacket.writeInt32(packet, component.getHealth());
    }

    @Override
    public void writeEntityUpdatedPacket(Entity entity, HealthComponentImpl component, OutputStream packet) {
        OutgoingPacket.writeInt32(packet, component.getMaxHealth());
        OutgoingPacket.writeInt32(packet, component.getHealth());

        OutgoingPacket.writeCollection(packet, component.getRecentHealthChangeInformation());
        component.getRecentHealthChangeInformation().clear();
    }
}
