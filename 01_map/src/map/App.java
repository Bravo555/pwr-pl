package map;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        System.out.println("Autor: Marcel Guzik");

        Map map;
        try {
            map = Map.loadFromFile("map.txt");
        } catch (MapException e) {
            System.out.println("invalid data: " + e.getLocalizedMessage());
            return;
        } catch (IOException e) {
            System.out.println("file error:");
            e.printStackTrace();
            return;
        }

        System.out.println("wczytano nastepujace dane:");
        System.out.println(map.toString());

        try {
            map.setName("Kraśnik");
            map.saveToFile("krasnik_map.txt");
        } catch (MapException e) {
            System.out.println("wrong map name!");
            return;
        } catch (IOException e) {
            System.out.println("error while writing to file:");
            e.printStackTrace();
        }

//        Camera camera = new Camera(640, 480);
//
//        try {
//            camera.selectMap(map);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // inspect a part of the map
//        camera.setOrigin(42.0f, 6.9f);
//
//        var pois = camera.getVisiblePois();
    }
}
