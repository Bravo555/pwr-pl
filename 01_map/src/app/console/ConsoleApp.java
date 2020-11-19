package app.console;

import app.map.Map;
import app.map.MapException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleApp {
    static Map map;

    public static void test(String[] args) {
        System.out.println("Autor: Marcel Guzik\n");
        printHelpMsg();
        System.out.println("\n");

        boolean exit = false;
        while(!exit) {
            String input = ConsoleUserDialog.enterString("> ");
            Scanner s = new Scanner(input);
            if(!s.hasNext()) continue;
            String cmd = s.next();

            switch (cmd) {
                case "h":
                    printHelpMsg();
                    break;
                case "q":
                    exit = true;
                    break;
                case "l":
                    String filename = s.next(); // TODO: add support for filenames with spaces
                    try {
                        map = Map.loadFromFile(filename);
                    } catch (MapException e) {
                        System.out.println("invalid data: " + e.getLocalizedMessage());
                        break;
                    } catch (IOException e) {
                        System.out.println("file error:");
                        e.printStackTrace();
                        break;
                    }
                    System.out.println("Map " + map.getName() + " loaded.");
                    break;
                case "s":
                    try {
                        String filename_rly = s.next(); // TODO: add support for filenames with spaces
                        map.saveToFile(filename_rly);
                    } catch (NoSuchElementException e) {
                        System.out.println("wrong format!");
                        printHelpMsg();
                    } catch (IOException e) {
                        System.out.println("file error:");
                        e.printStackTrace();
                        break;
                    }
                    break;
                case "a":
                    float x, y;
                    String name;
                    try {
                        x = s.nextFloat();
                        y = s.nextFloat();
                        s.skip(" ");
                        name = s.nextLine();
                    } catch (NoSuchElementException e) {
                        System.out.println("wrong format!");
                        printHelpMsg();
                        break;
                    }

                    map.addPointOfInterest(name, x, y);

                case "p":
                    System.out.println("Nazwa mapy: " + map.getName());
                    System.out.println("Wydawnictwo: " + map.getPublisher());
                    System.out.println(String.format("Wymiary: x: %d, y: %d",
                            map.getWidth(), map.getHeight(), map.getScale()));
                    System.out.println("Punkty zainteresowania:");
                    map.getPointsOfInterest().stream().forEach(
                        (poi) -> System.out.printf("\t%s - x: %f, y: %f\n",
                                poi.getName(), poi.getX(), poi.getY())
                    );
                    break;
                default:
                    printHelpMsg();
                    break;
            }
        }
    }

    static void printHelpMsg() {
        System.out.println("help:");
        System.out.println("l map_file.txt\t\tload the map from file");
        System.out.println("s map_file.txt\t\tsave the map to file");
        System.out.println("a x y name\t\t\tadd point of interest");
        System.out.println("p\t\t\t\t\tprint current map data");
        System.out.println("h\t\t\t\t\tthis help message");
        System.out.println("q\t\t\t\t\texit");

    }
}
