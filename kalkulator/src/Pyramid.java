public class Pyramid extends Figure implements Printable{

    private Figure base;
    private double h;

    Pyramid(Figure base, double h) throws InvalidDataException {
        if (base == null || h <=0 ) {
            throw new InvalidDataException("Wysokość musi być większa od 0.");
        }
        this.base = base;
        this.h = h;
    }

    @Override
    public void print() {
        System.out.println("-------------------------------------");
        System.out.println("Dane figury:\nh = " + h );
        System.out.println("-------------------------------------");
    }

    @Override
    public double calculateArea() {
        double baseArea = base.calculateArea();
        double lateralArea = 0;

        if (base instanceof Triangle) {
            double a = ((Triangle) base).getA();
            double b = ((Triangle) base).getB();
            double c = ((Triangle) base).getC();

            double slantHeight = Math.sqrt((h * h) + (baseArea / 2 * baseArea / 2));
            lateralArea = (a + b + c) * slantHeight / 2;

        } else if (base instanceof Rectangle) {
            double length = ((Rectangle) base).getA();
            double width = ((Rectangle) base).getB();
            double slantHeight = Math.sqrt((h * h) + ((length / 2) * (length / 2)));

            lateralArea = (length + width) * slantHeight;

        } else if (base instanceof Circle) {
            double radius = ((Circle) base).getR();
            double slantHeight = Math.sqrt((h * h) + (radius * radius));

            lateralArea = Math.PI * radius * slantHeight;
        }
        return baseArea + lateralArea;
    }

    @Override
    public double calculatePerimeter() {
        double perimeter;
        perimeter = base.calculatePerimeter();

        return perimeter;
    }

    public double calculateVolume() {
        double volume;
        volume = (base.calculateArea() * h) / 3;

        return volume;
    }
}