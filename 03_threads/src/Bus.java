import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

class Bus implements Runnable {
    public static final int MIN_BOARDING_TIME = 1000;
    public static final int MAX_BOARDING_TIME = 10000;
    public static final int GETTING_TO_BRIDGE_TIME = 500;
    public static final int CROSSING_BRIDGE_TIME = 3000;
    public static final int GETTING_PARKING_TIME = 500;
    public static final int UNLOADING_TIME = 500;

    private static int numberOfBuses = 0;


    // Metoda usypia wątek na podany czas w milisekundach
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Metoda usypia wątek na losowo dobrany czas
    // z przedziału [min, max) milisekund
    public static void sleep(int min_millis, int max_milis) {
        sleep(ThreadLocalRandom.current().nextInt(min_millis, max_milis));
    }


    NarrowBridge bridge;
    int id;
    BusDirection dir;


    Bus(NarrowBridge bridge) {
        this.bridge = bridge;
        this.id = ++numberOfBuses;
        if (ThreadLocalRandom.current().nextInt(0, 2) == 0)
            this.dir = BusDirection.EAST;
        else this.dir = BusDirection.WEST;
    }


    void printBusInfo(String message) {
        bridge.messageLog.append("Bus[" + id + "->"+dir+"]: " + message + "\n");
    }


    void boarding() {
        printBusInfo("Czeka na nowych pasażerów");
        sleep(MIN_BOARDING_TIME, MAX_BOARDING_TIME);
    }

    void goToTheBridge() {
        printBusInfo("Jazda w stronę mostu");
        sleep(GETTING_TO_BRIDGE_TIME);
    }

    void rideTheBridge() {
        printBusInfo("Przejazd przez most");
        sleep(CROSSING_BRIDGE_TIME);
    }

    void goToTheParking() {
        printBusInfo("Jazda w stronę końcowego parkingu");
        sleep(GETTING_PARKING_TIME);
    }

    void unloading() {
        printBusInfo("Rozładunek pasażerów");
        sleep(UNLOADING_TIME);
    }


    public void run() {
        bridge.allBuses.add(this);

        boarding();

        goToTheBridge();

        bridge.getOnTheBridge(this);

        rideTheBridge();

        bridge.getOffTheBridge(this);

        goToTheParking();

        unloading();
        bridge.allBuses.remove(this);
    }

    void draw(Graphics g) {

    }

}

