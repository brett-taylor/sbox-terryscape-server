package com.terryscape.game.worldobject;

import com.terryscape.cache.world.WorldObjectDefinition;
import com.terryscape.net.Client;

import java.util.Set;

public interface WorldObjectInteractionHandler {

    Set<String> getObjectId();

    void invoke(Client client, WorldObjectDefinition worldObjectDefinition);

}
