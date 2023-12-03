package com.terryscape.maths;

public class Vector2Int {

    private final int x;

    private final int y;

    public Vector2Int(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2Int add(Vector2Int other) {
        return new Vector2Int(getX() + other.getX(), getY() + other.getY());
    }

    public float distance(Vector2Int other) {
        int xd = other.x - x;
        int yd = other.y - y;

        return (float) Math.sqrt(xd * xd + yd * yd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2Int that = (Vector2Int) o;

        if (getX() != that.getX()) return false;
        return getY() == that.getY();
    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        return result;
    }
}
