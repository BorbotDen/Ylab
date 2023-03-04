import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void main(String[] args) {
        int number = new Random().nextInt(100); // здесь загадывается число от 1 до 99
        int maxAttempts = 10; // здесь задается количество попыток
        System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= maxAttempts; i++) {
            int userNumber = scanner.nextInt();
            if (number < userNumber) {
                System.out.println("Мое число меньше! Осталось  попыток " + (maxAttempts - i));
            } else if (number > userNumber) {
                System.out.println("Мое число больше! Осталось  попыток " + (maxAttempts - i));
            } else {
                System.out.println("Ты угадал с " + i + " попытки");
                return;
            }
        }
        System.out.println("Ты не угадал");
    }
}

