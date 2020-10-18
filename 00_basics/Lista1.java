import java.util.Scanner;
import java.util.function.Function;

class Lista1 {
    static Scanner s;

    public static void main(String[] args) {
        s = new Scanner(System.in);
        boolean exit = false;

        while(!exit) {
            System.out.println("wybierz zadanie:");
            System.out.println("7) zad 7");
            System.out.println("8) zad 8");
            System.out.println("9) zad 9");
            System.out.println("0) wyjdz z programu");

            int zadnum = s.nextInt();

            switch (zadnum) {
                case 0:
                    exit = true;
                    break;
                case 7:
                    zad7();
                    break;
                case 8:
                    zad8();
                    break;
                case 9:
                    zad9();
                    break;
                default:
                    System.out.println("Zly numer zadania! Sprobuj ponownie...");
            }
        }
    }

    static void zad9() {

        Scanner s = new Scanner(System.in);
        System.out.print("Poczatkowe n: ");
        int nInit = s.nextInt();
        int nCurrent = nInit, nNext;
        int i = 0;

        do {
            i++;
            String parity;
            if(nCurrent % 2 == 0) {
                nNext = nCurrent / 2;
                parity = "parzyste";
            } else {
                nNext = 3 * nCurrent + 1;
                parity = "nieparzyste";
            }

            System.out.println(String.format("%d, %d, %s, %d", i, nCurrent, parity, nNext));

            nCurrent = nNext;
        } while(nNext != 1);
    }

    static void zad8() {
        System.out.print("Podaj n: ");

        int n = s.nextInt();
        System.out.printf("Liczby pierwsze mniejsze od %d: %d\n", n, smallerPrimes(n));

    }

    static int smallerPrimes(int n) {
        int sum = 0;
        for(int j = 3; j < n; j++) {
            int facj2 = factorial(j - 2);
            sum += (facj2 - j * Math.floor(facj2 / j));
        }
        return -1 + sum;
    }

    // really? no factorial in stdlib??
    static int factorial(int n) {
        if(n <= 1) return 1;
        else return n * factorial(n-1);
    }

    static void zad7() {
        System.out.print("Podaj epsilon: ");

        double eps = s.nextDouble();
        System.out.println(series1(eps));
        System.out.println(series2(eps));
    }

    static double series1(double eps) {
        double sum = 0;
        double current = 9999;
        for(int k = 1; Math.abs(current) > eps; k++) {
            current = Math.pow(-1, k+1) * (1.0/(2.0*k - 1.0));
            sum += current;
        }
        return 4 * sum;
    }

    static double series2(double eps) {
        double sum = 0;
        double current = 9999;
        for(int k = 0; Math.abs(current) > eps; k++) {
            current = 1.0 / factorial(k);
            sum += current;
        }
        return sum;
    }
}
