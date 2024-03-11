package com.terryscape.game.npc;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnDeathEntityEvent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.NextTickTaskStep;
import com.terryscape.game.task.step.impl.WaitTaskStep;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.world.WorldManager;

import java.io.OutputStream;

public class NpcComponentImpl extends BaseEntityComponent implements NpcComponent {

    private NpcDefinition npcDefinition;

    public NpcComponentImpl(Entity entity) {
        super(entity);

        getEntity().subscribe(OnDeathEntityEvent.class, this::onDeath);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_npc";
    }

    @Override
    public NpcDefinition getNpcDefinition() {
        return npcDefinition;
    }

    public void setNpcDefinition(NpcDefinition npcDefinition) {
        this.npcDefinition = npcDefinition;
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, npcDefinition.getId());
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
    }

    private void onDeath(OnDeathEntityEvent onDeathEntityEvent) {
        getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation("death");

        getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
            WaitTaskStep.ticks(8),
            NextTickTaskStep.doThis(getEntity()::delete)
        );
    }
}
