package com.terryscape.game.projectile;

import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

@Singleton
public class ProjectileComponentComponentSystem extends ComponentSystem<ProjectileComponent> {

    @Override
    public Class<ProjectileComponent> forComponentType() {
        return ProjectileComponent.class;
    }

    @Override
    public void onTick(Entity entity, ProjectileComponent component) {
        component.decrementLifeSpanLeft();

        if (component.getLifeSpanLeft() < 0) {
            entity.delete();
        }
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_projectile";
    }

    @Override
    public void writeEntityAddedPacket(Entity entity, ProjectileComponent component, OutputStream packet) {
        OutgoingPacket.writeString(packet, component.getProjectileDefinition().getId());
        component.getEntitySource().getIdentifier().writeToPacket(packet);
        component.getEntityTarget().getIdentifier().writeToPacket(packet);
        OutgoingPacket.writeInt32(packet, component.getLifeSpan());
    }

}
