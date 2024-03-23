package com.terryscape.game.player;

import com.google.common.collect.Streams;
import com.terryscape.entity.component.BaseEntityComponent;

public class PlayerBonusesProviderComponentImpl extends BaseEntityComponent implements PlayerBonusesProviderComponent {

    private final PlayerComponent playerComponent;

    public PlayerBonusesProviderComponentImpl(PlayerComponent playerComponent) {
        this.playerComponent = playerComponent;
    }

    @Override
    public float getOffensiveStab() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getOffensiveStab())
            .sum();
    }

    @Override
    public float getOffensiveSlash() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getOffensiveSlash())
            .sum();
    }

    @Override
    public float getOffensiveAir() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getOffensiveAir())
            .sum();
    }

    @Override
    public float getOffensiveFire() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getOffensiveFire())
            .sum();
    }

    @Override
    public float getOffensiveArrow() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getOffensiveArrow())
            .sum();
    }

    @Override
    public float getDefensiveStab() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getDefensiveStab())
            .sum();
    }

    @Override
    public float getDefensiveSlash() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getDefensiveSlash())
            .sum();
    }

    @Override
    public float getDefensiveAir() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getDefensiveAir())
            .sum();
    }

    @Override
    public float getDefensiveFire() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getDefensiveFire())
            .sum();
    }

    @Override
    public float getDefensiveArrow() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getDefensiveArrow())
            .sum();
    }

    @Override
    public float getStrengthMelee() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getStrengthMelee())
            .sum();
    }

    @Override
    public float getStrengthMagic() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getStrengthMagic())
            .sum();
    }

    @Override
    public float getStrengthRange() {
        return (float) playerComponent.getEquipment().getAllItems().stream()
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getStrengthRange())
            .sum();
    }

    @Override
    public float getWeight() {
        return (float) Streams.concat(playerComponent.getInventory().getAllItems().stream(), playerComponent.getEquipment().getAllItems().stream())
            .mapToDouble(itemContainerItem -> itemContainerItem.getItemDefinition().getItemStatsDefinition().getWeight())
            .sum();
    }

}
