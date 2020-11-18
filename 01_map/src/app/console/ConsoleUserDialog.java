package app.console;

import java.util.Scanner;
import java.util.function.Function;

public class ConsoleUserDialog {
    private final static Scanner sc = new Scanner(System.in);

    public static String enterString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    public static<T> T prompt(String prompt, Function<String, T> func) {
        boolean isError;
        T t = null;
        do {
            isError = false;
            try {
                t = func.apply(enterString(prompt));
            } catch (Exception e) {
                System.err.println("Nieprawidlowe dane!\nSprobuj jeszcze raz.");
                isError = true;
            }
        } while(isError);
        return t;
    }

    public static int enterInt(String prompt) {
        return prompt(prompt, Integer::parseInt);
    }

    public static float enterFloat(String prompt) {
        return prompt(prompt, Float::parseFloat);
    }
}
