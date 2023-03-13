
/**
 * z1 = a1 + b1*i
 * z2 = a2 + b2*i
 */
public class ComplexNumber {

    private final double a;
    private final double bi;

    public ComplexNumber(double a) {
        this.a = a;
        this.bi = 0;
    }

    public ComplexNumber(double a, double bi) {
        this.a = a;
        this.bi = bi;
    }

    //z1+z2 = (a1 + a2) + (b1 + b2)i
    public ComplexNumber sum(ComplexNumber z2) {
        return new ComplexNumber(a + z2.a, bi + z2.bi);
    }

    //z1-z2 = (a1 - a2) + (b1 -b2)i
    public ComplexNumber subtract(ComplexNumber z2) {
        return new ComplexNumber(a - z2.a, bi - z2.bi);
    }

    //z1*z2=(a1*a2 + b1*b2) + i(a1*b2 + b1*a2)
    public ComplexNumber multiplication(ComplexNumber z2) {
        return new ComplexNumber((a * z2.a - bi * z2.bi), (a * z2.bi + z2.a * bi));
    }

    public double module() {
        return Math.sqrt(a * a + bi * bi);
    }

    @Override
    public String toString() {
        if (bi == 0) {
            return String.format("%.2f", a);
        } else if (a == 0) {
            return String.format("%.2fi", bi);
        } else {
            String sign = bi < 0 ? "-" : "+";
            return String.format("%.2f %s %.2fi", a, sign, Math.abs(bi));
        }

    }
}