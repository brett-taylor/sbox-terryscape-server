package com.terryscape.world;

public class Region {

    private final boolean[][] isWalkable;

    public Region() {
        isWalkable = new boolean[getRegionSize()][getRegionSize()];

        for (var y = 0; y < getRegionSize(); y++) {
            for (var x = 0; x < getRegionSize(); x++) {
                if (x == y && (x != 5 && x != 10)) {
                    setIsWalkable(x, y, false);
                    continue;
                }

                setIsWalkable(x, y, true);
            }
        }
    }

    public int getRegionSize() {
        return 20;
    }

    public boolean isWalkable(int x, int y) {
        return isWalkable[x][y];
    }

    public void setIsWalkable(int x, int y, boolean walkable) {
        isWalkable[x][y] = walkable;
    }
}
