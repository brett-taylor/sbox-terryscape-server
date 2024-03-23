package com.terryscape.game.grounditem;

import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;

@Singleton
public class GroundItemTimeAliveComponentSystem extends ComponentSystem<GroundItemTimeAliveComponent> {

    @Override
    public Class<GroundItemTimeAliveComponent> forComponentType() {
        return GroundItemTimeAliveComponent.class;
    }

    @Override
    public void onTick(Entity entity, GroundItemTimeAliveComponent component) {
        component.decrementTicksLeftAlive();

        if (component.getTicksLeftAlive() <= 0) {
            entity.delete();
        }
    }
}
