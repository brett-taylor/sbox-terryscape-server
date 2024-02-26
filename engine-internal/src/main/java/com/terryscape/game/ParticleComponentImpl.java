package com.terryscape.game;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.game.combat.ParticleComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.world.WorldManager;
import com.terryscape.world.coordinate.WorldCoordinate;

import java.io.OutputStream;

public class ParticleComponentImpl extends BaseEntityComponent implements ParticleComponent {

    public String getComponentIdentifier() {
        return "particle_added";
    }

    private WorldCoordinate target;
    private String imgUrl = "";
    private int duration = 0;
    private int currentTick = 0;

    public void setTarget(WorldCoordinate target) {
        this.target = target;
    }

    public void setImageUrl(String img) {
        imgUrl = img;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void tick() {
        super.tick();
        ++currentTick;
        if (currentTick > duration) {
            selfDestruct();
        }
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        target.writeToPacket(packet);
        OutgoingPacket.writeString(packet, imgUrl);
        OutgoingPacket.writeInt32(packet, duration);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, currentTick);
    }

    private final WorldManager worldManager;

    public ParticleComponentImpl(Entity entity, WorldManager worldManager) {
        super(entity);
        this.worldManager = worldManager;
    }

    private void selfDestruct() {
        getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
                ImmediateStep.run(() -> worldManager.deleteEntity(getEntity().getIdentifier()))
        );
    }
}
