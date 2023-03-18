public class SequencesImpl implements Sequences {
    private static final int ONE = 1;
    private static final int NEGATIVE_ONE = -1;
    private static final int ZERO = 0;


    private void toPrint(int i) {
        System.out.print(i + " ");
    }

    @Override
    public void a(int n) {
        int printCount = 0;
        int i = 1;
        while (printCount < n) {
            if (i % 2 == 0) {
                toPrint(i);
                printCount++;
            }
            i++;
        }
        System.out.println();
    }

    @Override
    public void b(int n) {
        int printCount = 0;
        int i = 1;
        while (printCount < n) {
            if (i % 2 == 1) {
                toPrint(i);
                printCount++;
            }
            i++;
        }
        System.out.println();
    }

    @Override
    public void c(int n) {
        int printCount = 0;
        int i = 1;
        while (printCount < n) {
            int squareNumber = i * i;
            toPrint(squareNumber);
            printCount++;
            i++;
        }
        System.out.println();
    }

    @Override
    public void d(int n) {
        int printCount = 0;
        int i = 1;
        while (printCount < n) {
            int cubeNumber = i * i * i;
            toPrint(cubeNumber);
            printCount++;
            i++;
        }
        System.out.println();
    }

    @Override
    public void e(int n) {
        int printCount = 0;
        int i = 1;
        while (printCount < n) {
            if (i % 2 == 0) {
                toPrint(NEGATIVE_ONE);
            } else {
                toPrint(ONE);
            }
            printCount++;
            i++;
        }
        System.out.println();
    }

    @Override
    public void f(int n) {
        int printCount = 0;
        int i = 1;
        while (printCount < n) {
            if (i % 2 == 0) {
                toPrint(NEGATIVE_ONE * i);
            } else {
                toPrint(i);
            }
            printCount++;
            i++;
        }
        System.out.println();
    }

    @Override
    public void g(int n) {
        int printCount = 0;
        int i = 1;
        while (printCount < n) {
            int SquareNumber = i * i;
            if (printCount % 2 == 0) {
                toPrint(SquareNumber);
            } else {
                toPrint(SquareNumber * NEGATIVE_ONE);
            }
            printCount++;
            i++;
        }
        System.out.println();
    }

    @Override
    public void h(int n) {
        int printCount = 0;
        int i = 1;
        int previousNum = 0;
        while (printCount < n) {
            if (previousNum == 0) {
                toPrint(i);
                previousNum = i;
                i++;
            } else {
                toPrint(ZERO);
                previousNum = ZERO;
            }
            printCount++;
        }
        System.out.println();
    }

    @Override
    public void i(int n) {
        int printCount = 0;
        int i = 1;
        int previousNum = 1;
        while (printCount < n) {
            toPrint(i * previousNum);
            previousNum = i * previousNum;
            printCount++;
            i++;
        }
        System.out.println();
    }

    @Override
    public void j(int n) {
        int printCount = 0;
        int previousNum = 0;
        int doublePreviousNum = 1;
        while (printCount < n) {
            int sum = previousNum + doublePreviousNum;
            toPrint(sum);
            doublePreviousNum = previousNum;
            previousNum = sum;
            printCount++;
        }
        System.out.println();
    }
}
