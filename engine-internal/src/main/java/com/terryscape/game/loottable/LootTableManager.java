package com.terryscape.game.loottable;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.cache.CacheLoader;
import com.terryscape.entity.EntityPrefabFactory;
import com.terryscape.game.item.ItemContainerItem;
import com.terryscape.game.movement.MovementComponent;
import com.terryscape.game.npc.NpcComponent;
import com.terryscape.maths.RandomUtil;
import com.terryscape.world.WorldManager;
import com.terryscape.world.coordinate.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Set;

@Singleton
public class LootTableManager {

    private static final Logger LOGGER = LogManager.getLogger(LootTableManager.class);

    private final CacheLoader cacheLoader;

    private final EntityPrefabFactory entityPrefabFactory;

    private final WorldManager worldManager;

    private final HashMap<String, LootTableProvider> lootTableProviders;

    @Inject
    public LootTableManager(Set<LootTableProvider> lootTables, CacheLoader cacheLoader, EntityPrefabFactory entityPrefabFactory, WorldManager worldManager) {
        this.cacheLoader = cacheLoader;
        this.entityPrefabFactory = entityPrefabFactory;
        this.worldManager = worldManager;

        lootTableProviders = new HashMap<>();
        lootTables.forEach(this::registerSingleLootTableProvider);
        LOGGER.info("Registered loot tables for {} npcs.", lootTableProviders.size());
    }

    public void createDropForNpc(NpcComponent npcComponent) {
        var lootTableProvider = lootTableProviders.get(npcComponent.getNpcDefinition().getId());
        if (lootTableProvider == null) {
            return;
        }

        var worldCoordinate = npcComponent.getEntity().getComponentOrThrow(MovementComponent.class).getWorldCoordinate();

        var lootTable = lootTableProvider.getLootTable(npcComponent.getNpcDefinition());

        lootTable.getGuaranteedDrops().forEach(lootTableItem -> createGroundItemForLootItem(worldCoordinate, lootTableItem));

        var randomLootItem = RandomUtil.randomCollection(lootTable.getOptionalDrops());
        createGroundItemForLootItem(worldCoordinate, randomLootItem);
    }

    private void registerSingleLootTableProvider(LootTableProvider lootTableProvider) {
        for (var npcId : lootTableProvider.getNpcIds()) {
            if (lootTableProviders.containsKey(npcId)) {
                throw new RuntimeException("A LootTableProvider can't be registered to npc %s as it already has one".formatted(npcId));
            }

            lootTableProviders.put(npcId, lootTableProvider);
        }
    }

    private void createGroundItemForLootItem(WorldCoordinate worldCoordinate, LootTableItem lootTableItem) {
        var amount = RandomUtil.randomNumber(lootTableItem.getMin(), lootTableItem.getMax());
        var itemContainer = new ItemContainerItem(lootTableItem.getItemDefinition(), amount);

        var groundItem = entityPrefabFactory.createGroundItemPrefab(itemContainer, worldCoordinate);
        worldManager.registerEntity(groundItem);
    }
}
