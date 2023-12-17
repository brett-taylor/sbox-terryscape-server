package com.terryscape.game.movement;

import com.terryscape.entity.Entity;
import com.terryscape.game.BaseEntityComponentImpl;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

public class AnimationComponentImpl extends BaseEntityComponentImpl implements AnimationComponent {

    private String playingAnimation;

    private boolean resetAnimation;

    public AnimationComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_animation_component";
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {

    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        OutgoingPacket.writeString(packet, playingAnimation);
        OutgoingPacket.writeBoolean(packet, resetAnimation);

        playingAnimation = null;
        resetAnimation = false;
    }

    @Override
    public void playAnimation(String animationName) {
        playingAnimation = animationName;
    }


    @Override
    public void resetAnimation() {
        resetAnimation = true;
    }
}
