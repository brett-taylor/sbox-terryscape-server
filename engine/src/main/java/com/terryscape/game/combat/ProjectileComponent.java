package com.terryscape.game.combat;

import com.terryscape.entity.component.EntityComponent;
import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.world.coordinate.WorldCoordinate;

public interface ProjectileComponent extends NetworkedEntityComponent {
    void setSource(WorldCoordinate target);
    void setTarget(WorldCoordinate target);
    void setImageUrl(String img);
    void setDuration(int duration);
}
