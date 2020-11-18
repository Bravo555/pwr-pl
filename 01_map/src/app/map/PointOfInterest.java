// Marcel Guzik
package app.map;

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

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "name='" + name + '\'' +
                ", point=" + point +
                '}';
    }

    private final String name;
    private final Vec2D point;
}
