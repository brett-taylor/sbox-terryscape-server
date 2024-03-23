package com.terryscape.game.player;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.maths.MathsUtil;

// TODO: Consider whether this should be moved into a regular java object onto the player?
// TODO: OR should the inventory and equipment be instead moved into their own components?
// TODO: Theres a bit of a mismatch between these two concepts but they are both just part of the player. So pick one.

public class PlayerSkillsComponentImpl extends BaseEntityComponent implements PlayerSkillsComponent {

    private int attack = 30;

    private int defence = 30;

    private int strength = 30;

    private int magic = 30;

    private int range = 30;

    private int constitution = 20;

    @Override
    public int getCombat() {
        float defensiveLevel = (getDefence() + getConstitution()) / 4f;

        float offensiveMeleeLevel = (getAttack() + getStrength()) * 0.325f;
        float offensiveMagicLevel = (getAttack() + getMagic()) * 0.325f;
        float offensiveRangeLevel = (getAttack() + getRange()) * 0.325f;

        float offensiveLevel = Math.max(offensiveRangeLevel, Math.max(offensiveMeleeLevel, offensiveMagicLevel));

        return MathsUtil.floorToInt(defensiveLevel + offensiveLevel);
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public void setAttack(int attack) {
        this.attack = attack;
    }

    @Override
    public int getDefence() {
        return defence;
    }

    @Override
    public void setDefence(int defence) {
        this.defence = defence;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public int getMagic() {
        return magic;
    }

    @Override
    public void setMagic(int magic) {
        this.magic = magic;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public int getConstitution() {
        return constitution;
    }

    @Override
    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

}
