package com.terryscape.game.equipment;

import java.util.Optional;

public enum EquipmentSlot {
    MAIN_HAND(0),
    OFF_HAND(1),
    TORSO(2);

    private final int slotId;

    EquipmentSlot(int slotId) {
        this.slotId = slotId;
    }

    public int getSlotId() {
        return slotId;
    }

    public static Optional<EquipmentSlot> tryParseFromSlotId(int slotId) {
        return switch (slotId) {
            case 0 -> Optional.of(MAIN_HAND);
            case 1 -> Optional.of(OFF_HAND);
            case 2 -> Optional.of(TORSO);
            default -> Optional.empty();
        };
    }
}
