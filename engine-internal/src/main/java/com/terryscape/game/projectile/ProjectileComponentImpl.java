package com.terryscape.game.projectile;

import com.terryscape.cache.projectile.ProjectileDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class ProjectileComponentImpl extends BaseEntityComponent implements ProjectileComponent {

    private final ProjectileDefinition projectileDefinition;

    private Entity entitySource;

    private Entity entityTarget;

    private int lifeSpan;

    private int lifeSpanLeft;

    public ProjectileComponentImpl(ProjectileDefinition projectileDefinition) {
        this.projectileDefinition = projectileDefinition;
    }

    @Override
    public String getComponentIdentifier() {
        return "component_projectile";
    }

    @Override
    public ProjectileDefinition getProjectileDefinition() {
        return projectileDefinition;
    }

    @Override
    public void setEntitySource(Entity entitySource) {
        this.entitySource = entitySource;
    }

    @Override
    public void setEntityTarget(Entity entityTarget) {
        this.entityTarget = entityTarget;
    }

    @Override
    public void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
        this.lifeSpanLeft = lifeSpan;
    }

    @Override
    public void tick() {
        super.tick();

        lifeSpanLeft -= 1;
        if (lifeSpanLeft < 0) {
            getEntity().delete();
        }
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, projectileDefinition.getId());
        entitySource.getIdentifier().writeToPacket(packet);
        entityTarget.getIdentifier().writeToPacket(packet);
        OutgoingPacket.writeInt32(packet, lifeSpan);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {

    }
}
