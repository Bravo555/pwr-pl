package map;

public class Main {
    public static void main(String[] args) {
        Map wroclawMap = Map.loadFromFile("wroclaw_map.txt");
        Camera camera = new Camera();

        try {
            camera.selectMap(wroclawMap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // inspect a part of the map
        camera.setOrigin(42.0f, 6.9f);

        var pois = camera.getVisiblePois();
    }
}
