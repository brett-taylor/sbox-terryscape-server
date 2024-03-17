package com.terryscape.cache;

import com.terryscape.cache.item.ItemDefinition;
import com.terryscape.cache.npc.NpcDefinition;
import com.terryscape.cache.object.ObjectDefinition;
import com.terryscape.cache.projectile.ProjectileDefinition;
import com.terryscape.cache.sound.SoundDefinition;
import com.terryscape.cache.world.WorldRegionDefinition;
import com.terryscape.game.world.coordinate.WorldRegionCoordinate;

import java.util.Optional;

public interface CacheLoader {

    ItemDefinition getItemDefinition(String id);

    Optional<ItemDefinition> getItemDefinitionSafe(String id);

    NpcDefinition getNpcDefinition(String id);

    Optional<NpcDefinition> getNpcDefinitionSafe(String id);

    ObjectDefinition getObjectDefinition(String id);

    Optional<WorldRegionDefinition> getWorldRegionDefinitionSafe(WorldRegionCoordinate worldRegionCoordinate);

    WorldRegionDefinition getWorldRegionDefinition(WorldRegionCoordinate worldRegionCoordinate);

    SoundDefinition getSoundDefinition(String id);

    Optional<SoundDefinition> getSoundDefinitionSafe(String id);

    ProjectileDefinition getProjectileDefinition(String id);

    Optional<ProjectileDefinition> getProjectileDefinitionSafe(String id);
}
