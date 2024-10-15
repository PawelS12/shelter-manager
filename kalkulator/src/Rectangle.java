public class Rectangle extends Figure implements Printable {

    private double a;
    private double b;

    Rectangle() {}
    Rectangle(double a, double b) throws InvalidDataException {
        if (a <= 0 || b <= 0) {
            throw new InvalidDataException("Wymiary boków musza być większe od 0.");
        }
        this.a = a;
        this.b = b;
    }

    public double getA() { return a; }

    public double getB() { return b; }

    @Override
    public void print() {
        System.out.println("-------------------------------------");
        System.out.println("Dane figury:\na = " + a + ", b = " + b);
        System.out.println("-------------------------------------");
    }

    @Override
    public double calculateArea() {
        double area;
        area = a * b;

        return area;
    }

    @Override
    public double calculatePerimeter() {
        double perimeter;
        perimeter = 2 * (a + b);

        return perimeter;
    }
}
