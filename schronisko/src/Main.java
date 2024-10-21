import java.util.List;

public class Main {
    public static void main(String[] args) {

        Animal animal_1 = new Animal("Pawel", "Kogut", AnimalCondition.KWARANTANNA, 221, 50001);
        animal_1.Print();
        Animal animal_2 = new Animal("Kawel", "Pies", AnimalCondition.KWARANTANNA, 221, 50001);
        animal_2.Print();
        Animal animal_3 = new Animal("Kasia", "Kaczka", AnimalCondition.CHORE, 21, 5000);
        animal_3.Print();
        Animal animal_4 = new Animal("bartek", "Kaczka", AnimalCondition.CHORE, 21, 5000);
        animal_4.Print();
        Animal animal_5 = new Animal("Mama", "Kaczka", AnimalCondition.CHORE, 21, 5000);
        animal_5.Print();
        Animal animal_6 = new Animal("Tata", "Kaczka", AnimalCondition.CHORE, 21, 5000);
        animal_6.Print();
        Animal animal_7 = new Animal("on", "Kaczka", AnimalCondition.CHORE, 21, 5000);
        animal_7.Print();


        AnimalShelter shelter = new AnimalShelter("Szkoła", 5);
        AnimalShelter shelter2 = new AnimalShelter("buda", 5);
        shelter.addAnimal(animal_1);
        shelter.addAnimal(animal_2);
        shelter.addAnimal(animal_3);
        shelter.addAnimal(animal_4);
        shelter.addAnimal(animal_5);
        shelter.addAnimal(animal_6);
        shelter.addAnimal(animal_7);

//        shelter.removeAnimal(animal_1);
//        shelter.getAnimal(animal_2);

        System.out.println("Ilość zwierząt na kwarantannie: " + shelter.countByCondition(AnimalCondition.KWARANTANNA));

        List<Animal> sortedAnimals  = shelter.sortByName();
        for (Animal animal : sortedAnimals) {
            System.out.println(animal.getName());
        }

        String searchName = "Kasia";
        String foundAnimalName = shelter.search(searchName);
        if (foundAnimalName != null) {
            System.out.println("Znaleziono zwierzę: " + foundAnimalName);
        } else {
            System.out.println("Nie znaleziono zwierzęcia o imieniu " + searchName);
        }


        String searchFragment = "awe";
        List<Animal> foundAnimals = shelter.searchPartial(searchFragment);

        if (!foundAnimals.isEmpty()) {
            System.out.println("Znalezione zwierzęta:");
            for (Animal animal : foundAnimals) {
                System.out.println(animal.getName() + " - Gatunek: " + animal.getSpecies());
            }
        } else {
            System.out.println("Nie znaleziono zwierząt pasujących do fragmentu: " + searchFragment);
        }

        shelter.summary();

        Animal mostExpensiveAnimal = shelter.max();
        if (mostExpensiveAnimal != null) {
            System.out.println("Najdroższe zwierzę: " + mostExpensiveAnimal.getName() + " - Cena: " + mostExpensiveAnimal.getPrice());
        }

        ShelterManager manager = new ShelterManager();
        manager.addShelter("aass",12);
        manager.removeShelter("ass");
        manager.addShelter("12", 123);

        List<String> emptyShelters = manager.findEmpty();
        if (emptyShelters.isEmpty()) {
            System.out.println("Brak pustych schronisk.");
        } else {
            System.out.println("Puste schroniska: " + emptyShelters);
        }

        manager.summary();

    }
}