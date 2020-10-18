package map;

// Autor: Marcel Guzik
// Data: 12 Pazdziernika 2020
// Grupa: Poniedzialek 14
//

import java.util.List;

public class Map {
    public Map(int width, int height, int scale, String name, String publisher) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.name = name;
        this.publisher = publisher;
    }

    public static Map loadFromFile(String filename) {
        return new Map(1, 1, 1, "hello there", "poggers");
    }

    public void writeToFile() {

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setName(String name) throws Exception {
        if(name == null || name.equals("")) {
            // TODO add some proper exception types
            throw new Exception("TODO");
        }
        this.name = name;
    }

    private void loadPointsOfInterest(List<Vec2D> pois) {
        pointsOfInterest = pois;
    }

    public List<Vec2D> getPointsOfInterest() {
        return pointsOfInterest;
    }

    private int width;
    private int height;
    // amount of real world distance unit equivalent to 1 map unit
    private final int scale;
    // name of the map
    private String name;
    // published of the map (idk what it's for)
    private String publisher;
    // points on the map
    private List<Vec2D> pointsOfInterest;
}