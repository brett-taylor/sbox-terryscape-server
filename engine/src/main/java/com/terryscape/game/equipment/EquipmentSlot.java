package com.terryscape.game.equipment;

import java.util.Optional;

public enum EquipmentSlot {
    MAIN_HAND(0),
    OFF_HAND(1),
    HELMET(2),
    TORSO(3),
    GLOVES(4),
    PANTS(5),
    SHOES(6);

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
            case 2 -> Optional.of(HELMET);
            case 3 -> Optional.of(TORSO);
            case 4 -> Optional.of(GLOVES);
            case 5 -> Optional.of(PANTS);
            case 6 -> Optional.of(SHOES);
            default -> Optional.empty();
        };
    }
}
