package com.terryscape.game.player;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.maths.MathsUtil;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;

// TODO: Consider whether this should be moved into a regular java object onto the player?
// TODO: OR should the inventory and equipment be instead moved into their own components?
// TODO: Theres a bit of a mismatch between these two concepts but they are both just part of the player. So pick one.

public class PlayerSkillsComponentImpl extends BaseEntityComponent implements PlayerSkillsComponent {

    private int attack = 30;

    private int defence = 30;

    private int strength = 30;

    private int magic = 30;

    private int constitution = 10;

    public PlayerSkillsComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_player_skills";
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        writePacket(packet);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        writePacket(packet);
    }

    @Override
    public int getCombat() {
        float defensiveLevel = (getDefence() + getConstitution()) / 4f;

        float offensiveMeleeLevel = (getAttack() + getStrength()) * 0.325f;
        float offensiveMagicLevel = (getAttack() + getMagic()) * 0.325f;

        float offensiveLevel = Math.max(offensiveMeleeLevel, offensiveMagicLevel);

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
    public int getConstitution() {
        return constitution;
    }

    @Override
    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    private void writePacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, getCombat());
        OutgoingPacket.writeInt32(packet, getAttack());
        OutgoingPacket.writeInt32(packet, getDefence());
        OutgoingPacket.writeInt32(packet, getStrength());
        OutgoingPacket.writeInt32(packet, getMagic());
        OutgoingPacket.writeInt32(packet, getConstitution());
    }
}
