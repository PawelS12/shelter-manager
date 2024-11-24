package com.example.shelterjavafx.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class AnimalShelter {
    private final StringProperty shelterName;
    private final IntegerProperty maxCapacity;
    private final IntegerProperty currentAnimals;
    private ObservableList<Animal> animals;  // Lista zwierząt obserwowana przez JavaFX

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = new SimpleStringProperty(shelterName);
        this.maxCapacity = new SimpleIntegerProperty(maxCapacity);
        this.currentAnimals = new SimpleIntegerProperty(0); // Początkowo brak zwierząt
        this.animals = FXCollections.observableArrayList();  // Inicjalizujemy ObservableList
    }

    public int getMaxCapacity() {
        return this.maxCapacity.get();
    }

    public String getShelterName() {
        return this.shelterName.get();
    }

    public ObservableList<Animal> getAnimals() {
        return animals;
    }

    public void setShelterName(String shelterName) {
        this.shelterName.set(shelterName);
    }

    public StringProperty shelterNameProperty() {
        return shelterName;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity.set(maxCapacity);
    }

    public IntegerProperty maxCapacityProperty() {
        return maxCapacity;
    }

    public int getCurrentAnimals() {
        return currentAnimals.get();
    }

    public IntegerProperty currentAnimalsProperty() {
        return currentAnimals;
    }

    // Dodawanie zwierzęcia do schroniska
    public boolean addAnimal(Animal animal) {
        for (Animal existingAnimal : animals) {
            if (existingAnimal.compareTo(animal) == 0) {
                System.err.println("To zwierzę już istnieje.");
                return false;
            }
        }

        if (animals.size() < maxCapacity.get()) {
            animals.add(animal);
            currentAnimals.set(animals.size());
            return true;
        } else {
            System.err.println("Pełna pojemność. Nie można dodać więcej zwierząt.");
            return false;
        }
    }

    // Usuwanie zwierzęcia
    public boolean removeAnimal(Animal animal) {
        boolean removed = animals.removeIf(existingAnimal -> existingAnimal.compareTo(animal) == 0);
        if (removed) {
            currentAnimals.set(animals.size());  // Aktualizacja liczby zwierząt
        }
        return removed;
    }

    // Adopcja zwierzęcia przez studentów
    public boolean adoptAnimal(Animal animal, Student student) {
        boolean removed = animals.removeIf(existingAnimal -> existingAnimal.compareTo(animal) == 0);
        if (removed) {
            animal.setAdopted();  // Oznaczamy zwierzę jako adoptowane
            student.getAnimals().add(animal);
            currentAnimals.set(animals.size());  // Aktualizacja liczby zwierząt
            System.out.println("Zwierzę zostało zaadoptowane.");
            return true;
        }
        System.err.println("Nie znaleziono zwierzęcia.");
        return false;
    }

    // Zmiana stanu zdrowia zwierzęcia
    public boolean changeCondition(Animal animal, AnimalCondition condition) {
        for (Animal existingAnimal : animals) {
            if (existingAnimal.compareTo(animal) == 0) {
                existingAnimal.setCondition(condition);
                return true;
            }
        }
        System.err.println("Nie znaleziono zwierzęcia.");
        return false;
    }

    // Zmiana wieku zwierzęcia
    public boolean changeAge(Animal animal, int age) {
        for (Animal existingAnimal : animals) {
            if (existingAnimal.compareTo(animal) == 0) {
                existingAnimal.setAge(age);
                return true;
            }
        }
        System.err.println("Nie znaleziono zwierzęcia.");
        return false;
    }

    // Liczenie zwierząt o danym stanie zdrowia
    public int countByCondition(AnimalCondition condition) {
        int counter = 0;
        for (Animal existingAnimal : animals) {
            if (existingAnimal.getCondition() == condition) {
                counter++;
            }
        }
        return counter;
    }

    // Sortowanie zwierząt po nazwie
    public List<Animal> sortByName() {
        List<Animal> sortedList = new ArrayList<>(animals);
        Collections.sort(sortedList, Comparator.comparing(Animal::getName));
        return sortedList;
    }

    // Sortowanie zwierząt po cenie
    public List<Animal> sortByPrice() {
        List<Animal> sortedList = new ArrayList<>(animals);
        Collections.sort(sortedList, Comparator.comparingDouble(Animal::getPrice));
        return sortedList;
    }

    // Wyszukiwanie zwierzęcia po nazwie
    public String search(String name) {
        Comparator<Animal> nameComparator = Comparator.comparing(Animal::getName, String::compareToIgnoreCase);

        for (Animal animal : animals) {
            if (nameComparator.compare(animal, new Animal(name, null, null, 0, 0)) == 0) {
                return animal.getName();
            }
        }
        System.err.println("Zwierzę o imieniu " + name + " nie zostało znalezione.");
        return null;
    }

    // Wyszukiwanie zwierzęcia po fragmencie nazwy lub gatunku
    public List<Animal> searchPartial(String fragment) {
        List<Animal> matchingAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getName().toLowerCase().contains(fragment.toLowerCase()) || animal.getSpecies().toLowerCase().contains(fragment.toLowerCase())) {
                matchingAnimals.add(animal);
            }
        }
        return matchingAnimals;
    }

    // Wyświetlanie wszystkich zwierząt w schronisku
    public void summary() {
        System.out.println("Wszystkie zwierzęta w schronisku: ");
        for (Animal animal : animals) {
            animal.Print();
        }
    }

    // Zwierzę o najwyższej cenie
    public Animal max() {
        if (animals.isEmpty()) {
            System.err.println("Brak zwierząt w schronisku.");
            return null;
        }

        return Collections.max(animals, Comparator.comparingDouble(Animal::getPrice));
    }

    public static List<AnimalShelter> sortSheltersByMaxCapacity(List<AnimalShelter> shelters) {
        Collections.sort(shelters, Comparator.comparingInt(AnimalShelter::getMaxCapacity));
        return shelters;
    }
}
