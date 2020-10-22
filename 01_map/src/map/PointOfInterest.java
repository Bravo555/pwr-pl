package map;

import java.util.Objects;

public class PointOfInterest {
    public PointOfInterest(String name, Vec2D point) {
        this.name = Objects.requireNonNull(name);
        this.point = Objects.requireNonNull(point);
    }

    public String getName() {
        return name;
    }

    public Vec2D getPoint() {
        return point;
    }

    final Vec2D point;
    final String name;
}
