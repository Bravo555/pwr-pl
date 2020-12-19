package app.map;

// Autor: Marcel Guzik
// Data: 12 Pazdziernika 2020
// Grupa: Poniedzialek 14
//

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map implements Serializable, Comparable {
    private static final long serialVersionUID = 1L;

    public Map() {
        this.width = 0;
        this.height = 0;
        this.scale = 1;
        this.name = "test map";
        this.publisher = "test publisher";
        this.pointsOfInterest = new ArrayList<>();
    }

    public Map(int width, int height, int scale, String name, String publisher) throws MapException {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.name = validateString(name);
        this.publisher = validateString(publisher);
        this.pointsOfInterest = new ArrayList<>();
    }

    public static Map loadFromFile(String filename) throws IOException, MapException {
        return loadFromFile(new File(filename));
    }

    public static Map loadFromFile(File file) throws IOException, MapException {
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Map map = (Map) inputStream.readObject();
            return map;
        } catch(ClassNotFoundException e) {
            // ... theoretically shouldn't happen...?
        }
        return null;
    }

    public void saveToFile(String filename) throws IOException {
        saveToFile(new File(filename));
    }

    public void saveToFile(File file) throws IOException {
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(this);
        }
    }

    public void addPointOfInterest(String name, float x, float y) {
        pointsOfInterest.add(new PointOfInterest(name, x, y));
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

    public void setPublisher(String publisher) throws MapException {
        this.publisher = validateString(publisher);
    }

    public void setDimensions(int x, int y) {
        this.width = x;
        this.height = y;
    }

    public void setScale(int scale) {
        this.scale = scale;
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

    @Override
    public int hashCode() {

        return Arrays.hashCode(new Object[]{
            width           ,
            height          ,
            scale           ,
            name            ,
            publisher       ,
            pointsOfInterest

        });

    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int compareTo(Object o) {
        // sort by width and height; from smallest maps to biggest maps
        Map other = (Map) o;
        return this.width * this.height - other.width * other.height;
    }

    private int width;
    private int height;
    // amount of real world distance unit equivalent to 1 map unit
    private int scale;
    // name of the map
    private String name;
    // published of the map (idk what it's for)
    private String publisher;
    // points on the map
    private List<PointOfInterest> pointsOfInterest;


}

