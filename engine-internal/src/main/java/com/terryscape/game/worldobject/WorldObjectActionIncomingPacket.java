package com.terryscape.game.worldobject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.net.Client;
import com.terryscape.net.IncomingPacket;
import com.terryscape.world.coordinate.WorldRegionCoordinate;

import java.nio.ByteBuffer;

@Singleton
public class WorldObjectActionIncomingPacket implements IncomingPacket {

    private final CacheLoader cacheLoader;

    private final WorldObjectInteractionDispatcher worldObjectInteractionDispatcher;

    @Inject
    public WorldObjectActionIncomingPacket(CacheLoader cacheLoader, WorldObjectInteractionDispatcher worldObjectInteractionDispatcher) {
        this.cacheLoader = cacheLoader;
        this.worldObjectInteractionDispatcher = worldObjectInteractionDispatcher;
    }

    @Override
    public String getPacketName() {
        return "client_server_world_object_action";
    }

    @Override
    public void handlePacket(Client client, ByteBuffer packet) {
        var worldObjectIdentifier = IncomingPacket.readString(packet);
        var worldRegionCoordinate = WorldRegionCoordinate.readFromPacket(packet);
        var action = IncomingPacket.readString(packet);

        var worldRegion = cacheLoader.getWorldRegionDefinition(worldRegionCoordinate);
        var worldObject = worldRegion.getWorldObjectDefinition(worldObjectIdentifier);
        var objectDefinition = worldObject.getObjectDefinition();

        var player = client.getPlayer().orElseThrow();

        // TODO: Refactor this examine into their own handlers
        
        if (action.equals("examine")) {
            var description = "%s (id=%s)".formatted(objectDefinition.getDescription(), objectDefinition.getId());
            player.getEntity().getComponentOrThrow(PlayerChatComponent.class).sendGameMessage(description);
        }

        // TODO check the player can interact with world objects currently?

        if (objectDefinition.isInteractable() && action.equals("interact")) {
            worldObjectInteractionDispatcher.dispatchWorldObjectInteraction(client, worldObject);
        }
    }
}
