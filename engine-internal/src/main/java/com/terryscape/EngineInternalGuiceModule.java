package com.terryscape;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.CacheLoaderImpl;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.entity.EntityPrefabFactoryImpl;
import com.terryscape.event.EventSystem;
import com.terryscape.event.EventSystemImpl;
import com.terryscape.game.chat.PlayerChatIncomingPacket;
import com.terryscape.game.equipment.PlayerEquipmentInterfaceHandler;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.interfaces.packet.InterfaceActionIncomingPacket;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.game.interfaces.InterfaceManagerImpl;
import com.terryscape.game.item.PlayerInventoryInterfaceHandler;
import com.terryscape.game.login.LoginIncomingPacket;
import com.terryscape.game.movement.WalkIncomingPacket;
import com.terryscape.game.npc.NpcActionIncomingPacket;
import com.terryscape.game.worldobject.WorldObjectActionIncomingPacket;
import com.terryscape.net.IncomingPacket;
import com.terryscape.net.PacketManager;
import com.terryscape.net.PacketManagerImpl;
import com.terryscape.world.WorldClock;
import com.terryscape.world.WorldClockImpl;
import com.terryscape.world.WorldManager;
import com.terryscape.world.WorldManagerImpl;
import com.terryscape.world.pathfinding.PathfindingManager;
import com.terryscape.world.pathfinding.PathfindingManagerImpl;

public class EngineInternalGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(Server.class).to(ServerImpl.class);
        binder().bind(PacketManager.class).to(PacketManagerImpl.class);
        binder().bind(EventSystem.class).to(EventSystemImpl.class);
        binder().bind(PathfindingManager.class).to(PathfindingManagerImpl.class);
        binder().bind(CacheLoader.class).to(CacheLoaderImpl.class);
        binder().bind(WorldClock.class).to(WorldClockImpl.class);
        binder().bind(WorldManager.class).to(WorldManagerImpl.class);
        binder().bind(EntityPrefabFactory.class).to(EntityPrefabFactoryImpl.class);
        binder().bind(InterfaceManager.class).to(InterfaceManagerImpl.class);

        var incomingPacketMultibinder = Multibinder.newSetBinder(binder(), IncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(LoginIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(WalkIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(PlayerChatIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(InterfaceActionIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(NpcActionIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(WorldObjectActionIncomingPacket.class);

        var interfaceActionHandlerMultibinder = Multibinder.newSetBinder(binder(), InterfaceActionHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(PlayerEquipmentInterfaceHandler.class);
        interfaceActionHandlerMultibinder.addBinding().to(PlayerInventoryInterfaceHandler.class);
    }

}
