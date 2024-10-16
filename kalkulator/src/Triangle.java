import static java.lang.Math.sqrt;

public class Triangle extends Figure implements Printable {

    private double a;
    private double b;
    private double c;
    private double h;

    Triangle() {}
    Triangle(double a, double b, double c) throws InvalidDataException {
        if (a <= 0 || b <= 0 || c <= 0) {
            throw new InvalidDataException("Wymiary boków musza być większe od 0.");
        }

        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new InvalidDataException("Suma długości dwóch boków musi być większa od długości trzeciego.");
        }

        this.a = a;
        this.b = b;
        this.c = c;
        this.h = heron(this.a, this.b, this.c);

        if (h <= 0) {
            throw new InvalidDataException("Wysokość musi być większa od 0.");
        }
    }

    public double getA() { return a; }

    public double getB() { return b; }

    public double getC() { return c; }

    public double getH() { return h; }

    public double heron(double a, double b, double c) {
        double s = (a + b + c) / 2;
        double A = Math.sqrt(s * (s - a) * (s - b) * (s - c));
        double h = (2 * A) / a;

        return h;
    }

    @Override
    public void print() {
        System.out.println("-------------------------------------");
        System.out.println("Dane figury:\na = " + a + ", b = " + b + ", c = " + c + ", h = " + h);
        System.out.println("-------------------------------------");
    }

    @Override
    public double calculateArea() {
        double area;
        area = 0.5 * a * h;

        return area;
    }

    @Override
    public double calculatePerimeter() {
        double perimeter;
        perimeter = a + b + c;

        return perimeter;
    }
}