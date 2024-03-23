package com.terryscape.game.animation;

import com.google.inject.Singleton;
import com.terryscape.entity.Entity;
import com.terryscape.entity.component.ComponentSystem;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

@Singleton
public class AnimationComponentSystem extends ComponentSystem<AnimationComponent> {

    @Override
    public Class<AnimationComponent> forComponentType() {
        return AnimationComponent.class;
    }

    @Override
    public boolean isNetworked() {
        return true;
    }

    @Override
    public String getComponentNetworkIdentifier() {
        return "component_animation";
    }

    @Override
    public void writeEntityUpdatedPacket(Entity entity, AnimationComponent component, OutputStream packet) {
        OutgoingPacket.writeString(packet, component.getPlayingAnimation());
        OutgoingPacket.writeBoolean(packet, component.shouldResetAnimation());

        component.setPlayingAnimation(null);
        component.setResetAnimation(false);
    }
}
