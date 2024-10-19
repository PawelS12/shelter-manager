public class Main {
    public static void main(String[] args) {

        Animal animal_1 = new Animal("Pawel", "Kogut", AnimalCondition.KWARANTANNA, 221, 50001, ComparisonType.NAME);
        animal_1.Print();
        Animal animal_2 = new Animal("Marek", "Kaczka", AnimalCondition.ZDROWE, 21, 5000, ComparisonType.NAME);
        animal_2.Print();
        Animal animal_3 = new Animal("Kasia", "Kaczka", AnimalCondition.CHORE, 21, 5000,ComparisonType.NAME);
        animal_3.Print();
        Animal animal_4 = new Animal("bartek", "Kaczka", AnimalCondition.CHORE, 21, 5000,ComparisonType.NAME);
        animal_4.Print();
        Animal animal_5 = new Animal("Mama", "Kaczka", AnimalCondition.CHORE, 21, 5000,ComparisonType.NAME);
        animal_5.Print();
        Animal animal_6 = new Animal("Tata", "Kaczka", AnimalCondition.CHORE, 21, 5000,ComparisonType.NAME);
        animal_6.Print();
        Animal animal_7 = new Animal("on", "Kaczka", AnimalCondition.CHORE, 21, 5000,ComparisonType.NAME);
        animal_7.Print();


        AnimalShelter shelter = new AnimalShelter("Szkoła", 5);
        shelter.addAnimal(animal_1);
        shelter.addAnimal(animal_2);
        shelter.addAnimal(animal_3);
        shelter.addAnimal(animal_4);
        shelter.addAnimal(animal_5);
        shelter.addAnimal(animal_6);
        shelter.addAnimal(animal_7);


    }
}