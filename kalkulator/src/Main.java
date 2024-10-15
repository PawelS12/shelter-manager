import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean isOpen = true;

        while(isOpen) {
            System.out.println("Witaj w kalkulatorze!");
            System.out.println("Wybierz opcje poniżej (poprzez wybranie liczby): ");
            System.out.println("1. Trójkąt");
            System.out.println("2. Okrąg");
            System.out.println("3. Prostokąt");
            System.out.println("4. Ostropsłup");
            System.out.println("0. Koniec\n");

            int number = scanner.nextInt();

            switch(number) {
                case 1: {
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

                    break;
                }
                case 2: {
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

                    break;
                }
                case 3: {
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

                    break;
                }
                case 4: {
                    try {
                        System.out.println("Wybierz podstawę ostrosłupa:");
                        System.out.println("1. Trójkąt");
                        System.out.println("2. Okrąg");
                        System.out.println("3. Prostokąt");
                        int baseChoice = scanner.nextInt();
                        Figure baseFigure = null;

                        switch (baseChoice) {
                            case 1: {
                                System.out.println("\nPodaj wymiary trójkąta: ");
                                System.out.print("bok a: ");
                                double a = scanner.nextDouble();
                                System.out.print("bok b: ");
                                double b = scanner.nextDouble();
                                System.out.print("bok c: ");
                                double c = scanner.nextDouble();
                                baseFigure = new Triangle(a, b, c);
                                break;
                            }
                            case 2: {
                                System.out.print("\nPodaj promień r: ");
                                double r = scanner.nextDouble();
                                baseFigure = new Circle(r);
                                break;
                            }
                            case 3: {
                                System.out.println("\nPodaj wymiary prostokąta: ");
                                System.out.print("bok a: ");
                                double width = scanner.nextDouble();
                                System.out.print("bok b: ");
                                double height = scanner.nextDouble();
                                baseFigure = new Rectangle(width, height);
                                break;
                            }
                            default: {
                                System.out.println("Niepoprawna opcja. Wybierz ponownie.");
                                break;
                            }
                        }
                        if (baseFigure != null) {
                            System.out.print("Podaj wysokość ostrosłupa: ");
                            double pyramidHeight = scanner.nextDouble();
                            System.out.println();
                            Pyramid pyramid = new Pyramid(baseFigure, pyramidHeight);
                            pyramid.print();
                            System.out.println("Pole powierzchni: " + pyramid.calculateArea());
                            System.out.println("Objętość: " + pyramid.calculateVolume());
                            System.out.println("-------------------------------------\n");
                        }
                    } catch (InvalidDataException e) {
                        System.out.println("Błąd: " + e.getMessage());
                    }
                    break;
            }
                case 0: {
                    isOpen = false;
                    break;
                }
                default: {
                    System.out.println("Niepoprawna opcja. Wybierz ponownie.");
                }
            }
        }

        scanner.close();

    }
}