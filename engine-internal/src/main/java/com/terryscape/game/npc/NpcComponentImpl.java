package com.terryscape.game.npc;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.EntityManager;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnEntityDeathEntityEvent;
import com.terryscape.game.movement.AnimationComponent;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateStep;
import com.terryscape.game.task.step.impl.WaitStep;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class NpcComponentImpl extends BaseEntityComponent implements NpcComponent {

    private final EntityManager entityManager;

    private NpcDefinition npcDefinition;

    private String npcVariant;

    public NpcComponentImpl(Entity entity, EntityManager entityManager) {
        super(entity);

        this.entityManager = entityManager;

        getEntity().subscribe(OnEntityDeathEntityEvent.class, this::onDeath);
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

    private void onDeath(OnEntityDeathEntityEvent onEntityDeathEntityEvent) {
        getEntity().getComponentOrThrow(AnimationComponent.class).playAnimation("death");

        getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
            WaitStep.ticks(8),
            ImmediateStep.run(() -> entityManager.deleteEntity(getEntity().getIdentifier()))
        );
    }
}
