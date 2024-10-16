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
        System.out.println("Wysokość ostrosłupa:\nh = " + h );
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

            double l1 = Math.sqrt(h * h + Math.pow(a / 2, 2));
            double l2 = Math.sqrt(h * h + Math.pow(b / 2, 2));
            double l3 = Math.sqrt(h * h + Math.pow(c / 2, 2));

            double lateral1 = (a * l1) / 2;
            double lateral2 = (b * l2) / 2;
            double lateral3 = (c * l3) / 2;

            lateralArea = lateral1 + lateral2 + lateral3;

        } else if (base instanceof Rectangle) {
            double a = ((Rectangle) base).getA();
            double b = ((Rectangle) base).getB();
            double l = Math.sqrt((h * h) + ((a / 2) * (a / 2)));

            lateralArea = (a + b) * l;

        } else if (base instanceof Circle) {
            double r = ((Circle) base).getR();
            double l = Math.sqrt((h * h) + (r * r));

            lateralArea = Math.PI * r * l;
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