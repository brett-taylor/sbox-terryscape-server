package com.terryscape.game.player;

import com.terryscape.entity.component.BaseEntityComponent;
import com.terryscape.game.appearance.HumanoidGender;
import com.terryscape.game.combat.health.HealthComponent;
import com.terryscape.game.equipment.PlayerEquipment;
import com.terryscape.game.equipment.PlayerEquipmentImpl;
import com.terryscape.game.item.FixedSizeItemContainer;
import com.terryscape.game.item.PlayerInventory;
import com.terryscape.net.Client;

public class PlayerComponentImpl extends BaseEntityComponent implements PlayerComponent {

    private final FixedSizeItemContainer inventory;

    private final PlayerEquipment equipment;

    private Client client;

    private String username;

    private String steamId;

    private HumanoidGender gender;

    private float specialAttackPower;

    private boolean wantsToSpecialAttack;

    public PlayerComponentImpl() {
        inventory = new PlayerInventory();
        equipment = new PlayerEquipmentImpl();

        specialAttackPower = 100f;
    }

    @Override
    public Client getClient() {
        return client;
    }

    public PlayerComponentImpl setClient(Client client) {
        this.client = client;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    @Override
    public FixedSizeItemContainer getInventory() {
        return inventory;
    }

    @Override
    public PlayerEquipment getEquipment() {
        return equipment;
    }

    @Override
    public void setGender(HumanoidGender gender) {
        this.gender = gender;
    }

    @Override
    public HumanoidGender getGender() {
        return gender;
    }

    @Override
    public float getSpecialAttackPower() {
        return specialAttackPower;
    }

    @Override
    public void setSpecialAttackPower(float specialAttackPower) {
        this.specialAttackPower = specialAttackPower;
    }

    @Override
    public boolean wantsToSpecialAttack() {
        return wantsToSpecialAttack;
    }

    @Override
    public void setWantsToSpecialAttack(boolean wantsToSpecialAttack) {
        this.wantsToSpecialAttack = wantsToSpecialAttack;
    }

    @Override
    public boolean canDoActions() {
        return !getEntity().getComponentOrThrow(HealthComponent.class).isDying();
    }

}
