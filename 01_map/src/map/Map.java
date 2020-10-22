package map;

// Autor: Marcel Guzik
// Data: 12 Pazdziernika 2020
// Grupa: Poniedzialek 14
//

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map {
    public Map(int width, int height, int scale, String name, String publisher) throws MapException {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.name = validateString(name);
        this.publisher = validateString(publisher);
        this.pointsOfInterest = new ArrayList<>();
    }

    public static Map loadFromFile(String filename) throws IOException, MapException {
        Scanner scanner = new Scanner(new File(filename));

        int width = scanner.nextInt();
        int height = scanner.nextInt();
        int scale = scanner.nextInt();
        scanner.skip("\n");
        String name = scanner.nextLine();
        String publisher = scanner.nextLine();
        List<PointOfInterest> pois = new ArrayList<>();
        Map map = new Map(width, height, scale, name, publisher);
        map.pointsOfInterest = pois;
        if(!scanner.hasNext()) {
            return map;
        }
        scanner.skip("\n");
        while (scanner.hasNextLine()) {
            float x = scanner.nextFloat();
            float y = scanner.nextFloat();
            scanner.skip(" "); // remove space after last coordinate
            String poiName = scanner.nextLine();
            PointOfInterest poi = new PointOfInterest(poiName, new Vec2D(x, y));
            pois.add(poi);
        }

        return map;
    }

    public void saveToFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(String.format("%d %d %d\n%s\n%s\n\n", width, height, scale, name, publisher));
        for(PointOfInterest poi: pointsOfInterest) {
            writer.write(String.format("%f %f %s\n", poi.getPoint().x(), poi.getPoint().y(), poi.getName()));
        }
        writer.close();
    }

    public void addPointOfInterest(String name, float x, float y) {
        Vec2D point = new Vec2D(x, y);
        pointsOfInterest.add(new PointOfInterest(name, point));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScale() {
        return scale;
    }

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setName(String name) throws MapException {
        this.name = validateString(name);
    }

    public String validateString(String str) throws MapException {
        if(str == null || str.equals("") || str.contains("\n")) {
            throw new MapException("Name can't be null or empty or contain newlines");
        }
        return str;
    }

    public void addPointOfInterest(PointOfInterest poi) {
        pointsOfInterest.add(poi);
    }

    public List<PointOfInterest> getPointsOfInterest() {
        return pointsOfInterest;
    }

    @Override
    public String toString() {
        return "Map {" +
                "width=" + width +
                ", height=" + height +
                ", scale=" + scale +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pointsOfInterest=" + pointsOfInterest +
                '}';
    }

    private final int width;
    private final int height;
    // amount of real world distance unit equivalent to 1 map unit
    private final int scale;
    // name of the map
    private String name;
    // published of the map (idk what it's for)
    private String publisher;
    // points on the map
    private List<PointOfInterest> pointsOfInterest;
}

