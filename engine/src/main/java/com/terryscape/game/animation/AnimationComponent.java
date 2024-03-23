package com.terryscape.game.animation;

import com.terryscape.entity.component.BaseEntityComponent;

public class AnimationComponent extends BaseEntityComponent {

    private String playingAnimation;

    private boolean resetAnimation;

    public String getPlayingAnimation() {
        return playingAnimation;
    }

    public void setPlayingAnimation(String playingAnimation) {
        this.playingAnimation = playingAnimation;
    }

    public boolean shouldResetAnimation() {
        return resetAnimation;
    }

    public void setResetAnimation(boolean resetAnimation) {
        this.resetAnimation = resetAnimation;
    }

}
