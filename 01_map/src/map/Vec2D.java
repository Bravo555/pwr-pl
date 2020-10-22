package map;

public final class Vec2D {
    public Vec2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    float x() { return x; }
    float y() { return y; }

    @Override
    public String toString() {
        return "Vec2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    private final float x;
    private final float y;
}
