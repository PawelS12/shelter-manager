import java.util.List;

public class Main {
    public static void main(String[] args) {

        ShelterManager shelterManager = new ShelterManager();

        // Stworzenie schronisk
        shelterManager.addShelter("Schronisko1", 25);
        shelterManager.addShelter("Schronisko2", 15);
        System.out.println();

        AnimalShelter schronisko1 = shelterManager.getShelter("Schronisko1");

        // Stworzenie zwierząt
        Animal dog = new Animal("Pluto", "Pies", AnimalCondition.zdrowe, 4, 600.0);
        Animal cat = new Animal("Kasia", "Kot", AnimalCondition.chore, 3, 500.0);
        Animal rabbit = new Animal("Marysia", "Królik", AnimalCondition.kwarantanna, 1, 400.0);

        // Dodanie zwierząt do schroniska
        schronisko1.addAnimal(dog);
        schronisko1.addAnimal(cat);
        schronisko1.addAnimal(rabbit);

        // Dodanie tego samego zwierzecia - błąd
        schronisko1.addAnimal(dog);

        // Zmiana stanu zwierzecia
        schronisko1.changeCondition(cat, AnimalCondition.zdrowe);

        // Zmiana wieku zwierzęcia
        schronisko1.changeAge(rabbit, 3);

        // Wyliczenie zdrowych zwierząt
        int healthyCount = schronisko1.countByCondition(AnimalCondition.zdrowe);
        System.out.println("Ilość zdrowych zwierząt: " + healthyCount);
        System.out.println();

        // Posortowanie zwierząt po imieniu
        List<Animal> sortedByName = schronisko1.sortByName();
        System.out.println("Zwierzęta posortowane po imieniu:");
        for (Animal a : sortedByName) {
            a.Print();
        }
        System.out.println();

        // Posortowanie zwierząt po cenie
        List<Animal> sortedByPrice = schronisko1.sortByPrice();
        System.out.println("Zwierzęta posortowane po cenie:");
        for (Animal a : sortedByPrice) {
            a.Print();
        }
        System.out.println();

        // Wyszukanie zwierzęcia po imieniu
        String foundAnimalName = schronisko1.search("Pluto");
        if (foundAnimalName != null) {
            System.out.println("Znaleziono zwierzę: " + foundAnimalName);
        }
        System.out.println();

        // Wyszukanie zwierzęcia po fragmencie
        List<Animal> matchingAnimals = schronisko1.searchPartial("ka");
        System.out.println("Zwierzęta z fragmentem 'ka':");
        for (Animal a : matchingAnimals) {
            a.Print();
        }
        System.out.println();

        // Wyświetlenie podsumowania schroniska
        schronisko1.summary();
        System.out.println();

        // Znalezioenie najdroższego zwierzęcia
        Animal mostExpensiveAnimal = schronisko1.max();
        if (mostExpensiveAnimal != null) {
            System.out.println("Najdroższe zwierzę:");
            mostExpensiveAnimal.Print();
        }
        System.out.println();

        // Usunięcie zwierzęcia
        boolean removed = schronisko1.removeAnimal(dog);
        System.out.println("Usunięto zwierzę: " + removed);
        System.out.println();

        // Stworzenie studenta
        Student student = new Student("Paweł", "Socała");

        // Adopcja zwierzęcia
        boolean adopted = schronisko1.getAnimal(cat, student);

        // Wyświetlenie zwierząd adoptopwanych przez danego stuenta
        System.out.println("Zwierzęta adoptowane przez studenta: ");
        student.displayAnimals();
        System.out.println();

        // Znalezioenie pustych schronisk
        List<String> emptyShelters = shelterManager.findEmpty();
        System.out.print("Puste schroniska: ");
        for (String shelterName : emptyShelters) {
            System.out.println(shelterName);
        }
        System.out.println();

        // Wyświetlenie podsumowanie managera schronisk
        shelterManager.summary();
        System.out.println();

        // Usunięcie schroniska przez managera schronisk
        boolean shelterRemoved = shelterManager.removeShelter("Schronisko1");
        System.out.println("Usunięto schronisko: " + shelterRemoved);
        System.out.println();

    }
}