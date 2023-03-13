public class ComplexNumbersTest {
    public static void main(String[] args) {
        ComplexNumber z1 = new ComplexNumber(5, -6);
        ComplexNumber z2 = new ComplexNumber(-2);
        System.out.println("z1 = " + z1);
        System.out.println("z2 = " + z2);
        System.out.println("z1 + z2 = " + z1.sum(z2));
        System.out.println("z1 * z2 = " + z1.multiplication(z2));
        System.out.println("z1 - z2 = " + z1.subtract(z2));
        System.out.println("module z1 = " + z1.module());
    }
}