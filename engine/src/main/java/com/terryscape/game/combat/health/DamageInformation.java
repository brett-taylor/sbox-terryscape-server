package com.terryscape.game.combat.health;

import com.terryscape.game.combat.DamageType;

public class DamageInformation {

    private int amount;

    private DamageType type;

    private boolean blocked;

    public DamageInformation() {
        amount = 0;
        type = DamageType.TYPELESS;
    }

    public int getAmount() {
        return amount;
    }

    public DamageInformation setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public DamageType getType() {
        return type;
    }

    public DamageInformation setType(DamageType type) {
        this.type = type;
        return this;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public DamageInformation setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }

}
