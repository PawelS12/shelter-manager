public class Circle extends Figure implements Printable {

    private double r;
    private double pi = Math.PI;

    Circle () {}
    Circle(double r) throws InvalidDataException {
        if (r <= 0) {
            throw new InvalidDataException("Promień musi być większy od 0.");
        }
        this.r = r;
    }

    public double getR() { return r; }

    @Override
    public void print() {
        System.out.println("-------------------------------------");
        System.out.println("Dane figury:\nr = " + r);
        System.out.println("-------------------------------------");
    }

    @Override
    public double calculateArea() {
        double area;
        area = pi * r * r;

        return area;
    }

    @Override
    public double calculatePerimeter() {
        double perimeter;
        perimeter = 2 * pi * r;

        return perimeter;
    }
}