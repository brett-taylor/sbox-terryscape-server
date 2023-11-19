package com.terryscape.game.combat.health;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HealthComponentImpl extends BaseEntityComponent implements HealthComponent {

    private final List<DamageInformation> recentDamage = new ArrayList<>();

    private int maxHealth;

    private int health;

    public HealthComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void setMaxHealth(int newMaxHealth) {
        maxHealth = newMaxHealth;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int newHealth) {
        health = newHealth;
    }

    @Override
    public void takeDamage(DamageInformation damageInformation) {
        health = health - damageInformation.getAmount();

        recentDamage.add(damageInformation);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_health_component";
    }

    @Override
    public void writeEntityAddedPacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, maxHealth);
        OutgoingPacket.writeInt32(packet, health);
    }

    @Override
    public void writeEntityUpdatedPacket(OutputStream packet) {
        OutgoingPacket.writeInt32(packet, maxHealth);
        OutgoingPacket.writeInt32(packet, health);

        OutgoingPacket.writeInt32(packet, recentDamage.size());
        recentDamage.forEach(damageInformation -> damageInformation.writeToPacket(packet));

        recentDamage.clear();
    }
}
