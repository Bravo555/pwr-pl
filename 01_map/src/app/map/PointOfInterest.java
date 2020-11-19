// Marcel Guzik
package app.map;

import java.io.*;
import java.util.Objects;

public class PointOfInterest implements Serializable {
    private static final long serialVersionUID = 2L;
    public PointOfInterest(String name, float x, float y) {
        this.name = Objects.requireNonNull(name);
        this.x = x;
        this.y = y;
    }

    public static PointOfInterest loadFromFile(File file) throws IOException {
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            PointOfInterest poi = (PointOfInterest) inputStream.readObject();
            return poi;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
       this.name = Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "name='" + name + '\'' +
                ", x =" + x +
                ", y =" + y +
                '}';
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    private String name;
    private float x;
    private float y;
}
