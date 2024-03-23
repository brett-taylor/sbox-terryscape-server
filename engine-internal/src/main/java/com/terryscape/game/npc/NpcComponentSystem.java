package com.terryscape.game.npc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.entity.event.type.OnDeathEntityEvent;
import com.terryscape.game.animation.AnimationComponent;
import com.terryscape.game.loottable.LootTableManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateTaskStep;
import com.terryscape.game.task.step.impl.NextTickTaskStep;
import com.terryscape.game.task.step.impl.WaitTaskStep;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

@Singleton
public class NpcComponentSystem extends ComponentSystem<NpcComponent> {

    private final LootTableManager lootTableManager;

    @Inject
    public NpcComponentSystem(LootTableManager lootTableManager) {
        this.lootTableManager = lootTableManager;
    }

    @Override
    public Class<NpcComponent> forComponentType() {
        return NpcComponent.class;
    }

    @Override
    public void onRegistered(Entity entity, NpcComponent component) {
         entity.subscribe(OnDeathEntityEvent.class, ignored -> onDeath(component));
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_npc";
    }

    @Override
    public void writeEntityAddedPacket(Entity entity, NpcComponent component, OutputStream packet) {
        OutgoingPacket.writeString(packet, component.getNpcDefinition().getId());
    }

    private void onDeath(NpcComponent component) {
        component.getEntity().getComponentOrThrow(AnimationComponent.class).setPlayingAnimation("death");

        component.getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
            WaitTaskStep.ticks(8),

            ImmediateTaskStep.doThis(() -> lootTableManager.createDropForNpc(component)),

            NextTickTaskStep.doThis(() -> component.getEntity().delete())
        );
    }
}
