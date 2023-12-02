package com.terryscape.game.movement;

import com.terryscape.entity.component.NetworkedEntityComponent;

public interface AnimationComponent extends NetworkedEntityComponent {

    void playAnimation(String animationName);

    void resetAnimation();

}
