import java.util.*;

public class AnimalShelter {
    private String shelterName;
    private List<Animal> animalList;
    private int maxCapacity;

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = shelterName;
        this.maxCapacity = maxCapacity;
        this.animalList = new ArrayList<>();
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public String getShelterName() {
        return this.shelterName;
    }

    public List<Animal> getAnimalList() {
        return this.animalList;
    }

    public boolean addAnimal(Animal animal) {
        for (Animal existingAnimal : animalList) {
            if (existingAnimal.compareTo(animal) == 0) {
                System.err.println("To zwierzę juz istnieje.");
                return false;
            }
        }

        if (animalList.size() < maxCapacity) {
            animalList.add(animal);
            return true;
        } else {
            System.err.println("Pełna pojemność. Nie można dodać więcej zwierząt.");
            return false;
        }
    }

    public boolean removeAnimal(Animal animal) {
        boolean removed = animalList.removeIf(existingAnimal -> existingAnimal.compareTo(animal) == 0);
        return removed;
    }

    public boolean getAnimal(Animal animal, Student student) {
        for (Animal existingAnimal : animalList) {
            if (existingAnimal.compareTo(animal) == 0) {
                existingAnimal.setAdopted();
                animalList.remove(existingAnimal);
                student.getAnimals().add(existingAnimal);

                System.out.println("Zwierzę zostało zaadoptowane.");
                return true;
            }
        }
        System.err.println("Nie znaleziono zwierzęcia.");
        return false;
    }

    public boolean changeCondition(Animal animal, AnimalCondition condition) {
        for (Animal existingAnimal : animalList) {
            if (existingAnimal.compareTo(animal) == 0) {
                existingAnimal.setCondition(condition);

                return true;
            }
        }
        System.err.println("Nie znaleziono zwierzęcia.");
        return false;
    }

    public boolean changeAge(Animal animal, int age) {
        for (Animal existingAnimal : animalList) {
            if (existingAnimal.compareTo(animal) == 0) {
                existingAnimal.setAge(age);

                return true;
            }
        }
        System.err.println("Nie znaleziono zwierzęcia.");
        return false;
    }

    public int countByCondition(AnimalCondition condition) {
        int counter = 0;

        for (Animal existingAnimal : animalList) {
            if(existingAnimal.getCondition() == condition) {
                counter++;
            }
        }
        return counter;
    }

    public List<Animal> sortByName() {
        List<Animal> sortedList = new ArrayList<>(animalList);
        Collections.sort(sortedList, new Comparator<Animal>() {
            @Override
            public int compare(Animal animal1, Animal animal2) {
                return animal1.getName().compareTo(animal2.getName());
            }
        });
        return sortedList;
    }

    public List<Animal> sortByPrice() {
        List<Animal> sortedList = new ArrayList<>(animalList);
        Collections.sort(sortedList, new Comparator<Animal>() {
            @Override
            public int compare(Animal animal1, Animal animal2) {
                return Double.compare(animal1.getPrice(), animal2.getPrice());
            }
        });
        return sortedList;
    }

    public String search(String name) {
        Comparator<Animal> nameComparator = new Comparator<Animal>() {
            @Override
            public int compare(Animal a1, Animal a2) {
                return a1.getName().compareToIgnoreCase(a2.getName());
            }
        };

        for (Animal animal : animalList) {
            if (nameComparator.compare(animal, new Animal(name, null, null, 0, 0)) == 0) {
                return animal.getName();
            }
        }
        System.err.println("Zwierzę o imieniu " + name + " nie zostało znalezione.");
        return null;
    }

    public List<Animal> searchPartial(String fragment) {
        List<Animal> matchingAnimals = new ArrayList<>();

        for (Animal animal : animalList) {
            if (animal.getName().toLowerCase().contains(fragment.toLowerCase()) || animal.getSpecies().toLowerCase().contains(fragment.toLowerCase())) {
                matchingAnimals.add(animal);
            }
        }

        return matchingAnimals;
    }

    public void summary() {
        System.out.println("Wszystkie zwierzęta w schonisku: ");
        for (Animal animal : animalList) {
            animal.Print();
        }
    }

    public Animal max() {
        if (animalList.isEmpty()) {
            System.err.println("Brak zwierząt w schronisku.");
            return null;
        }

        return Collections.max(animalList, new Comparator<Animal>() {
            @Override
            public int compare(Animal a1, Animal a2) {
                return Double.compare(a1.getPrice(), a2.getPrice());
            }
        });
    }

}