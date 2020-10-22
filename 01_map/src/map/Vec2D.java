package map;

public final class Vec2D {
    public Vec2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    float x() { return x; }
    float y() { return y; }

    final float x;
    final float y;
}
