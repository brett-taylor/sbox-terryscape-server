package com.terryscape.system.equipment;

import java.util.Optional;

public enum EquipmentSlot {
    MAIN_HAND(0),
    OFF_HAND(1);

    private final int slotId;

    EquipmentSlot(int slotId) {
        this.slotId = slotId;
    }

    public int getSlotId() {
        return slotId;
    }

    public static Optional<EquipmentSlot> tryParseFromSlotId(int slotId) {
        if (slotId == 0) {
            return Optional.of(MAIN_HAND);
        } else if (slotId == 1) {
            return Optional.of(OFF_HAND);
        }

        return Optional.empty();
    }
}
