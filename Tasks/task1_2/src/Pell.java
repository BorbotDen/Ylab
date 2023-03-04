import java.util.Scanner;

public class Pell {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int     pell = 0,
                    n0 = 0,
                    n1 = 1;
            if (n == n0) {
                System.out.println(n0);
            } else if (n == n1) {
                System.out.println(n1);
            } else {
                for (int i = 2; i <= n; i++) {
                    pell = 2 * n1 + n0;
                    n0 = n1;
                    n1 = pell;
                }
                System.out.println(pell);
            }
        }
    }
}