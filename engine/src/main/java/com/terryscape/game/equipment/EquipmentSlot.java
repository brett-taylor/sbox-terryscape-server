package com.terryscape.game.equipment;

public enum EquipmentSlot {
    MAIN_HAND(0),
    OFF_HAND(1),

    HEAD(2),
    TORSO(3),
    LEGS(4),
    HANDS(5),
    FEET(6);

    private final int slotId;

    EquipmentSlot(int slotId) {
        this.slotId = slotId;
    }

    public int getSlotId() {
        return slotId;
    }

    public static EquipmentSlot parseFromSlotId(int slotId) {
        return switch (slotId) {
            case 0 -> MAIN_HAND;
            case 1 -> OFF_HAND;
            case 2 -> HEAD;
            case 3 -> TORSO;
            case 4 -> LEGS;
            case 5 -> HANDS;
            case 6 -> FEET;
            default -> throw new IndexOutOfBoundsException(slotId);
        };

    }
}
