package com.terryscape.game.npc;

import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

@Singleton
public class SimpleNpcAppearanceComponentSystem extends ComponentSystem<SimpleNpcAppearanceComponent> {

    @Override
    public Class<SimpleNpcAppearanceComponent> forComponentType() {
        return SimpleNpcAppearanceComponent.class;
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_simple_npc_appearance";
    }

    @Override
    public void writeEntityAddedPacket(Entity entity, SimpleNpcAppearanceComponent component, OutputStream packet) {
        OutgoingPacket.writeString(packet, component.getVariant());
    }

}
