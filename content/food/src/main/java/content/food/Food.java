package content.food;

public enum Food {
    CHEESE("food_cheese", 10, 1, 4),
    FISH("food_fish", 25, 3, 4),
    CHICKEN("food_chicken", 20, 3, 4),
    PIE("food_pie", 50, 4, 6),
    POTATO("food_potato", 5, 1, 4);

    private final String itemId;

    private final int healAmount;

    private final int eatDelayTicks;

    private final int combatDelayTicks;

    Food(String itemId, int healAmount, int eatDelayTicks, int combatDelayTicks) {
        this.itemId = itemId;
        this.healAmount = healAmount;
        this.eatDelayTicks = eatDelayTicks;
        this.combatDelayTicks = combatDelayTicks;
    }

    public String getItemId() {
        return itemId;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public int getEatDelayTicks() {
        return eatDelayTicks;
    }

    public int getCombatDelayTicks() {
        return combatDelayTicks;
    }
}
