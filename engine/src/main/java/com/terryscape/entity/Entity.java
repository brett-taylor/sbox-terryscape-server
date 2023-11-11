package com.terryscape.entity;

import java.util.Optional;

/**
 * TODO: Entity structure
 * Entity
 *   Mob
 *     Player
 *     Npc
 *   GroundItem
 *   Projectile
 */
public interface Entity {

    Optional<EntityIdentifier> getIdentifier();

    EntityType getEntityType();

}
