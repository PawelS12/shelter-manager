import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private boolean isOpen;

    Menu() {
        this.scanner = new Scanner(System.in);
        this.isOpen = true;
    }

    public void showMenu() {
        while (isOpen) {
            System.out.println("Witaj w kalkulatorze!");
            System.out.println("Wybierz opcje poniżej (poprzez wybranie liczby): ");
            System.out.println("1. Trójkąt");
            System.out.println("2. Okrąg");
            System.out.println("3. Prostokąt");
            System.out.println("4. Ostrosłup");
            System.out.println("0. Koniec\n");

            int number = scanner.nextInt();

            handleOption(number);
        }
    }

    private void handleOption(int number) {
        switch (number) {
            case 1:
                handleTriangleOption();
                break;
            case 2:
                handleCircleOption();
                break;
            case 3:
                handleRectangleOption();
                break;
            case 4:
                handlePyramidOption();
                break;
            case 0:
                isOpen = false;
                break;
            default:
                System.out.println("Niepoprawna opcja. Wybierz ponownie.");
        }
    }

    private void handleTriangleOption() {
        try {
            System.out.println("Podaj wymiary figury: ");
            System.out.print("bok a: ");
            double a = scanner.nextDouble();
            System.out.print("bok b: ");
            double b = scanner.nextDouble();
            System.out.print("bok c: ");
            double c = scanner.nextDouble();

            Triangle triangle = new Triangle(a, b, c);
            System.out.println();
            triangle.print();
            System.out.println("Pole powierzchni: " + triangle.calculateArea());
            System.out.println("Obwód: " + triangle.calculatePerimeter());
            System.out.println("-------------------------------------\n");
        } catch (InvalidDataException e) {
            System.out.println("Błąd: " + e.getMessage());
            System.out.println();
        }
    }

    private void handleCircleOption() {
        try {
            System.out.println("Podaj wymiary figury: ");
            System.out.print("promień r: ");
            double r = scanner.nextDouble();

            Circle circle = new Circle(r);
            System.out.println();
            circle.print();
            System.out.println("Pole powierzchni: " + circle.calculateArea());
            System.out.println("Obwód: " + circle.calculatePerimeter());
            System.out.println("-------------------------------------\n");
        } catch (InvalidDataException e) {
            System.out.println("Błąd: " + e.getMessage());
            System.out.println();
        }
    }

    private void handleRectangleOption() {
        try {
            System.out.println("Podaj wymiary figury: ");
            System.out.print("bok a: ");
            double a = scanner.nextDouble();
            System.out.print("bok b: ");
            double b = scanner.nextDouble();

            Rectangle rectangle = new Rectangle(a, b);
            System.out.println();
            rectangle.print();
            System.out.println("Pole powierzchni: " + rectangle.calculateArea());
            System.out.println("Obwód: " + rectangle.calculatePerimeter());
            System.out.println("-------------------------------------\n");
        } catch (InvalidDataException e) {
            System.out.println("Błąd: " + e.getMessage());
            System.out.println();
        }
    }

    private void handlePyramidOption() {
        try {
            System.out.println("Wybierz podstawę ostrosłupa:");
            System.out.println("1. Trójkąt");
            System.out.println("2. Okrąg");
            System.out.println("3. Prostokąt");
            int number2 = scanner.nextInt();
            Figure baseFigure = null;

            switch (number2) {
                case 1:
                    System.out.println("\nPodaj wymiary trójkąta: ");
                    System.out.print("bok a: ");
                    double a = scanner.nextDouble();
                    System.out.print("bok b: ");
                    double b = scanner.nextDouble();
                    System.out.print("bok c: ");
                    double c = scanner.nextDouble();
                    baseFigure = new Triangle(a, b, c);
                    break;
                case 2:
                    System.out.print("\nPodaj promień r: ");
                    double r = scanner.nextDouble();
                    baseFigure = new Circle(r);
                    break;
                case 3:
                    System.out.println("\nPodaj wymiary prostokąta: ");
                    System.out.print("bok a: ");
                    a = scanner.nextDouble();
                    System.out.print("bok b: ");
                    b = scanner.nextDouble();
                    baseFigure = new Rectangle(a, b);
                    break;
                default:
                    System.out.println("Niepoprawna opcja. Wybierz ponownie.");
                    return;
            }

            if (baseFigure != null) {
                System.out.print("Podaj wysokość ostrosłupa: ");
                double h = scanner.nextDouble();
                System.out.println();
                Pyramid pyramid = new Pyramid(baseFigure, h);
                pyramid.print();
                System.out.println("Pole powierzchni: " + pyramid.calculateArea());
                System.out.println("Objętość: " + pyramid.calculateVolume());
                System.out.println("-------------------------------------\n");
            }
        } catch (InvalidDataException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    public void close() {
        scanner.close();
    }
}