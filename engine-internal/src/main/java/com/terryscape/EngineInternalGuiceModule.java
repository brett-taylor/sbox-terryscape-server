package com.terryscape;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.CacheLoaderImpl;
import com.terryscape.entity.EntityManager;
import com.terryscape.entity.EntityManagerImpl;
import com.terryscape.event.EventListener;
import com.terryscape.event.EventSystem;
import com.terryscape.event.EventSystemImpl;
import com.terryscape.event.MockEventListener;
import com.terryscape.game.chat.PlayerChatIncomingPacket;
import com.terryscape.game.interfaces.InterfaceActionIncomingPacket;
import com.terryscape.game.login.LoginIncomingPacket;
import com.terryscape.game.movement.WalkIncomingPacket;
import com.terryscape.net.IncomingPacket;
import com.terryscape.net.PacketManager;
import com.terryscape.net.PacketManagerImpl;
import com.terryscape.world.pathfinding.PathfindingManager;
import com.terryscape.world.pathfinding.PathfindingManagerImpl;

public class EngineInternalGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(Server.class).to(ServerImpl.class);
        binder().bind(PacketManager.class).to(PacketManagerImpl.class);
        binder().bind(EntityManager.class).to(EntityManagerImpl.class);
        binder().bind(EventSystem.class).to(EventSystemImpl.class);
        binder().bind(PathfindingManager.class).to(PathfindingManagerImpl.class);
        binder().bind(CacheLoader.class).to(CacheLoaderImpl.class);

        var incomingPacketMultibinder = Multibinder.newSetBinder(binder(), IncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(LoginIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(WalkIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(PlayerChatIncomingPacket.class);
        incomingPacketMultibinder.addBinding().to(InterfaceActionIncomingPacket.class);

        var eventListenerMultibinder = Multibinder.newSetBinder(binder(), EventListener.class);
        eventListenerMultibinder.addBinding().to(MockEventListener.class);
    }

    @Provides
    public static Gson provideGson() {
        return new Gson();
    }
}
