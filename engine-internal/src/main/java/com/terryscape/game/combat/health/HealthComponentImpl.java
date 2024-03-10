package com.terryscape.game.combat.health;

import com.terryscape.entity.Entity;
import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.entity.event.type.OnDeathEntityEvent;
import com.terryscape.net.OutgoingPacket;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HealthComponentImpl extends BaseEntityComponent implements HealthComponent {

    private final List<HealthChangeInformation> recentHealthChangeInformation = new ArrayList<>();

    private int maxHealth;

    private int health;

    public HealthComponentImpl(Entity entity) {
        super(entity);
    }

    @Override
    public String getComponentIdentifier() {
        return "component_health";
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

        if (health <= 0) {
            health = 0;
            handleDeath();
        }
    }

    @Override
    public boolean isDying() {
        return health <= 0;
    }

    @Override
    public void takeDamage(DamageInformation damageInformation) {
        if (isDying()) {
            return;
        }

        var cappedDamage = Math.min(damageInformation.getAmount(), health);
        damageInformation.setAmount(cappedDamage);

        health -= cappedDamage;

        recentHealthChangeInformation.add(createHealthChangeInformationFromDamageInformation(damageInformation));

        if (health <= 0) {
            health = 0;
            handleDeath();
        }
    }

    @Override
    public void heal(int amount) {
        if (isDying()) {
            return;
        }

        var cappedHealAmount = Math.min(amount, maxHealth - health);
        health += cappedHealAmount;

        var healthChangeInformation = new HealthChangeInformation()
            .setHealthChangeType(HealthChangeInformationType.HEAL_FOOD)
            .setAmount(cappedHealAmount);

        recentHealthChangeInformation.add(healthChangeInformation);
    }

    @Override
    public void resetHealthToMax() {
        this.health = this.maxHealth;
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

        OutgoingPacket.writeCollection(packet, recentHealthChangeInformation);
        recentHealthChangeInformation.clear();
    }

    private void handleDeath() {
        getEntity().invoke(OnDeathEntityEvent.class, new OnDeathEntityEvent());
    }

    private HealthChangeInformation createHealthChangeInformationFromDamageInformation(DamageInformation damageInformation) {
        HealthChangeInformationType healthChangeInformationType;
        if (damageInformation.isBlocked()) {
            healthChangeInformationType = HealthChangeInformationType.BLOCKED;
        } else {
            healthChangeInformationType = HealthChangeInformationType.toHealthChangeReason(damageInformation.getType());
        }

        return new HealthChangeInformation()
            .setHealthChangeType(healthChangeInformationType)
            .setAmount(damageInformation.getAmount());
    }
}
