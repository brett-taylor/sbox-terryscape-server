package com.terryscape.game.combat;

import com.terryscape.entity.component.NetworkedEntityComponent;
import com.terryscape.world.coordinate.WorldCoordinate;

public interface ParticleComponent extends NetworkedEntityComponent {
    void setTarget(WorldCoordinate target);
    void setImageUrl(String img);
    void setDuration(int duration);
}
