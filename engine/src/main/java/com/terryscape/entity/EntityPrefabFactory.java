package com.terryscape.entity;

import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.game.item.ItemContainerItem;
import com.terryscape.world.coordinate.WorldCoordinate;

public interface EntityPrefabFactory {

    Entity createNpcPrefab(NpcDefinition npcDefinition);

    Entity createPlayerPrefab();

    Entity createGroundItemPrefab(ItemContainerItem itemContainerItem, WorldCoordinate worldCoordinate);

}
