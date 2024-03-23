package com.terryscape.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.entity.event.type.OnDeathEntityEvent;
import com.terryscape.game.animation.AnimationComponent;
import com.terryscape.game.chat.PlayerChatSystem;
import com.terryscape.game.combat.OnAttackEntityEvent;
import com.terryscape.game.combat.OnAttackedEntityEvent;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.login.SetLocalPlayerOutgoingPacket;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.task.TaskComponent;
import com.terryscape.game.task.step.impl.ImmediateTaskStep;
import com.terryscape.game.task.step.impl.NextTickTaskStep;
import com.terryscape.game.task.step.impl.WaitTaskStep;
import com.terryscape.game.world.Direction;
import com.terryscape.game.world.coordinate.WorldCoordinate;
import com.terryscape.net.OutgoingPacket;
import com.terryscape.net.PacketManager;

import java.io.OutputStream;

@Singleton
public class PlayerComponentSystem extends ComponentSystem<PlayerComponentImpl> {

    private final PacketManager packetManager;

    private final SoundManager soundManager;

    private final SoundDefinition combatHitSoundDefinition;

    private final PlayerChatSystem playerChatSystem;

    private final SoundDefinition combatDeathSoundDefinition;

    @Inject
    public PlayerComponentSystem(PacketManager packetManager,
                                 SoundManager soundManager,
                                 @Named("combat_hit") SoundDefinition combatHitSoundDefinition,
                                 PlayerChatSystem playerChatSystem,
                                 @Named("combat_death") SoundDefinition combatDeathSoundDefinition) {

        this.packetManager = packetManager;
        this.soundManager = soundManager;
        this.combatHitSoundDefinition = combatHitSoundDefinition;
        this.playerChatSystem = playerChatSystem;
        this.combatDeathSoundDefinition = combatDeathSoundDefinition;
    }

    @Override
    public Class<PlayerComponentImpl> forComponentType() {
        return PlayerComponentImpl.class;
    }

    @Override
    public void onRegistered(Entity entity, PlayerComponentImpl component) {
        entity.subscribe(OnDeathEntityEvent.class, ignored -> onDeath(component));
        entity.subscribe(OnAttackEntityEvent.class, ignored -> onAttack(component));
        entity.subscribe(OnAttackedEntityEvent.class, ignored -> onAttacked(component));

        respawn(component);

        var setLocalEntityPacket = new SetLocalPlayerOutgoingPacket().setLocalEntity(component);
        packetManager.send(component.getClient(), setLocalEntityPacket);
    }

    @Override
    public void onTick(Entity entity, PlayerComponentImpl component) {
        if (component.getSpecialAttackPower() < 100) {
            component.setSpecialAttackPower(component.getSpecialAttackPower() + 0.5f);
        }
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_player";
    }

    @Override
    public void writeEntityAddedPacket(Entity entity, PlayerComponentImpl component, OutputStream packet) {
        OutgoingPacket.writeString(packet, component.getUsername());
        component.getInventory().writeToPacket(packet);
        component.getEquipment().writeToPacket(packet);
        OutgoingPacket.writeEnum(packet, component.getGender());
        OutgoingPacket.writeFloat(packet, component.getSpecialAttackPower());
    }

    @Override
    public void writeEntityUpdatedPacket(Entity entity, PlayerComponentImpl component, OutputStream packet) {
        component.getInventory().writeToPacket(packet);
        component.getEquipment().writeToPacket(packet);
        OutgoingPacket.writeEnum(packet, component.getGender());
        OutgoingPacket.writeFloat(packet, component.getSpecialAttackPower());
        OutgoingPacket.writeBoolean(packet, component.wantsToSpecialAttack());
    }

    private void respawn(PlayerComponentImpl component) {
        component.getEntity().getComponentOrThrow(AnimationComponent.class).setResetAnimation(true);
        component.getEntity().getComponentOrThrow(MovementComponent.class).teleport(new WorldCoordinate(14, 20));
        component.getEntity().getComponentOrThrow(MovementComponent.class).look(Direction.SOUTH);

        component.getEntity().getComponentOrThrow(HealthComponent.class).resetHealthToMax();
    }

    private void onDeath(PlayerComponentImpl component) {
        playerChatSystem.sendGameMessage(component, "You died!");
        component.getEntity().getComponentOrThrow(AnimationComponent.class).setPlayingAnimation("death");

        component.getEntity().getComponentOrThrow(TaskComponent.class).setPrimaryTask(
            WaitTaskStep.ticks(3),
            ImmediateTaskStep.doThis(() -> soundManager.playSoundEffect(component.getClient(), combatDeathSoundDefinition)),
            WaitTaskStep.ticks(5),
            NextTickTaskStep.doThis(() -> respawn(component))
        );
    }

    private void onAttack(PlayerComponentImpl component) {
        soundManager.playSoundEffect(component.getClient(), combatHitSoundDefinition);
    }

    private void onAttacked(PlayerComponentImpl component) {
        soundManager.playSoundEffect(component.getClient(), combatHitSoundDefinition);
    }
}
