import java.util.ArrayList;
import java.util.List;

public class AnimalShelter {
    private String shelterName;
    private List<Animal> animalList;
    private int maxCapacity;

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = shelterName;
        this.maxCapacity = maxCapacity;
        this.animalList = new ArrayList<>();
    }

    public boolean addAnimal(Animal animal) {
        for (Animal existingAnimal : animalList) {
            if (existingAnimal.compareTo(animal) == 0) {
                System.err.println("An animal with the same name already exists in the shelter.");
                return false;
            }
        }

        if (animalList.size() < maxCapacity) {
            animalList.add(animal);
            return true;
        } else {
            System.err.println("Full capacity. Cannot add more animals.");
            return false;
        }
    }
}