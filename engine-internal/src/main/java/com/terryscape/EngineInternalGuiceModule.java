package com.terryscape;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.entity.EntityPrefabFactoryImpl;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.event.EventSystem;
import com.terryscape.event.EventSystemImpl;
import com.terryscape.game.animation.AnimationComponentSystem;
import com.terryscape.game.chat.PlayerChatIncomingPacket;
import com.terryscape.game.chat.PlayerChatSystem;
import com.terryscape.game.chat.PlayerChatSystemImpl;
import com.terryscape.game.chat.command.Command;
import com.terryscape.game.chat.dialogue.DialogueInterfaceActionHandler;
import com.terryscape.game.chat.dialogue.DialogueManager;
import com.terryscape.game.chat.dialogue.DialogueManagerImpl;
import com.terryscape.game.diceroll.CombatDiceRoll;
import com.terryscape.game.diceroll.CombatDiceRollImpl;
import com.terryscape.game.equipment.PlayerEquipmentInterfaceActionHandler;
import com.terryscape.game.grounditem.GroundItemActionIncomingPacket;
import com.terryscape.game.grounditem.GroundItemComponentSystem;
import com.terryscape.game.grounditem.GroundItemTimeAliveComponentSystem;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.interfaces.InterfaceManagerImpl;
import com.terryscape.game.interfaces.packet.InterfaceActionIncomingPacket;
import com.terryscape.game.item.ItemInteractionHandler;
import com.terryscape.game.item.PlayerInventoryInterfaceActionHandler;
import com.terryscape.game.login.LoginIncomingPacket;
import com.terryscape.game.loottable.LootTableProvider;
import com.terryscape.game.movement.MovementOrbInterfaceActionHandler;
import com.terryscape.game.movement.WalkIncomingPacket;
import com.terryscape.game.npc.NpcActionIncomingPacket;
import com.terryscape.game.npc.NpcInteractionHandler;
import com.terryscape.game.player.PlayerActionIncomingPacket;
import com.terryscape.game.projectile.ProjectileComponentComponentSystem;
import com.terryscape.game.projectile.ProjectileFactory;
import com.terryscape.game.projectile.ProjectileFactoryImpl;
import com.terryscape.game.shop.ShopInterfaceActionHandler;
import com.terryscape.game.shop.ShopManager;
import com.terryscape.game.shop.ShopManagerImpl;
import com.terryscape.game.sound.SoundManager;
import com.terryscape.game.sound.SoundManagerImpl;
import com.terryscape.game.specialattack.SpecialAttackHandler;
import com.terryscape.game.specialattack.SpecialAttackOrbInterfaceActionHandler;
import com.terryscape.game.worldobject.WorldObjectActionIncomingPacket;
import com.terryscape.game.worldobject.WorldObjectInteractionHandler;
import com.terryscape.net.IncomingPacket;
import com.terryscape.net.PacketManager;
import com.terryscape.net.PacketManagerImpl;
import com.terryscape.game.world.WorldClock;
import com.terryscape.game.world.WorldClockImpl;
import com.terryscape.entity.EntityManager;
import com.terryscape.entity.EntityManagerImpl;
import com.terryscape.game.world.pathfinding.PathfindingManager;
import com.terryscape.game.world.pathfinding.PathfindingManagerImpl;

public class EngineInternalGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(Server.class).to(ServerImpl.class);
        binder().bind(PacketManager.class).to(PacketManagerImpl.class);
        binder().bind(EventSystem.class).to(EventSystemImpl.class);
        binder().bind(PathfindingManager.class).to(PathfindingManagerImpl.class);
        binder().bind(WorldClock.class).to(WorldClockImpl.class);
        binder().bind(EntityManager.class).to(EntityManagerImpl.class);
        binder().bind(EntityPrefabFactory.class).to(EntityPrefabFactoryImpl.class);
        binder().bind(InterfaceManager.class).to(InterfaceManagerImpl.class);
        binder().bind(ShopManager.class).to(ShopManagerImpl.class);
        binder().bind(DialogueManager.class).to(DialogueManagerImpl.class);
        binder().bind(CombatDiceRoll.class).to(CombatDiceRollImpl.class);
        binder().bind(SoundManager.class).to(SoundManagerImpl.class);
        binder().bind(ProjectileFactory.class).to(ProjectileFactoryImpl.class);
        binder().bind(PlayerChatSystem.class).to(PlayerChatSystemImpl.class);

        var incomingPacketMultibinder = Multibinder.newSetBinder(binder(), IncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(LoginIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(WalkIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(PlayerChatIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(InterfaceActionIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(NpcActionIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(WorldObjectActionIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(PlayerActionIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(GroundItemActionIncomingPacket.class);

        var interfaceActionHandlerMultibinder = Multibinder.newSetBinder(binder(), InterfaceActionHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(PlayerEquipmentInterfaceActionHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(PlayerInventoryInterfaceActionHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(DialogueInterfaceActionHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(ShopInterfaceActionHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(SpecialAttackOrbInterfaceActionHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(MovementOrbInterfaceActionHandler.class);

        var componentSystemMultibinder = Multibinder.newSetBinder(binder(), ComponentSystem.class);
        componentSystemMultibinder.addBinding().to(GroundItemComponentSystem.class);
        componentSystemMultibinder.addBinding().to(GroundItemTimeAliveComponentSystem.class);
        componentSystemMultibinder.addBinding().to(ProjectileComponentComponentSystem.class);
        componentSystemMultibinder.addBinding().to(AnimationComponentSystem.class);

        Multibinder.newSetBinder(binder(), Command.class);
        Multibinder.newSetBinder(binder(), ItemInteractionHandler.class);
        Multibinder.newSetBinder(binder(), LootTableProvider.class);
        Multibinder.newSetBinder(binder(), NpcInteractionHandler.class);
        Multibinder.newSetBinder(binder(), SpecialAttackHandler.class);
        Multibinder.newSetBinder(binder(), WorldObjectInteractionHandler.class);
        Multibinder.newSetBinder(binder(), ComponentSystem.class);
    }

}
