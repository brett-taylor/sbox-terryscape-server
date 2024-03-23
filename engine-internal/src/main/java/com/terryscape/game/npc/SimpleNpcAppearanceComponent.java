package com.terryscape.game.npc;

import com.terryscape.entity.component.BaseEntityComponent;

public class SimpleNpcAppearanceComponent extends BaseEntityComponent {

    private String variant;

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

}
