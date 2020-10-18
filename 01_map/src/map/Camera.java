package map;

import java.util.List;
import java.util.stream.Collectors;

public class Camera {
    private float x;
    private float y;
    private float width;
    private float height;
    // zoom amount of 1 means map is displayed in its native scale; >1 map is zoomed in; <1 map is zoomed out
    private float zoom;
    private Map map;

    public void selectMap(Map map) throws Exception {
        if(map == null) {
            throw new Exception("map can't be null");
        }
        this.map = map;
    }

    // sets the camera origin, i.e. its center point, to the provided coordinates of the currently selected map
    public void setOrigin(float x, float y) {
        if(map == null) {
            return;
        }
        this.x = Math.min(x, map.getWidth());
        this.y = Math.min(y, map.getHeight());
    }

    public List<Vec2D> getVisiblePois() {
        return map.getPointsOfInterest().stream().filter(this::isVisible).collect(Collectors.toUnmodifiableList());
    }

    // is the point under provided absolute coordinates visible by the camera
    public boolean isVisible(Vec2D point) {
        // TODO: replace with some smart and funky matrix math
        float x_view_left = x - width * (1/zoom);
        float x_view_right = x + width * (1/zoom);
        float y_view_top = y - height * (1/zoom);
        float y_view_bottom = y + height * (1/zoom);

        return x_view_left < point.x && point.x < x_view_right &&
                y_view_bottom < point.y && point.y < y_view_top;
    }
}
