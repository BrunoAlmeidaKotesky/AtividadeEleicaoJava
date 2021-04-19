package helpers;
import java.util.Scanner;

public class Inputs {
    public String input(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        String value = scanner.nextLine();
        return value;
    }

    public int intput(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        int value = scanner.nextInt();
        return value;
    }

    public double doubleInput(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        double value = scanner.nextDouble();
        return value;
    }
}
