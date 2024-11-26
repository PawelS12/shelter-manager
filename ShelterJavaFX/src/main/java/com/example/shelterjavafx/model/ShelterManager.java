package com.example.shelterjavafx.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShelterManager {
    private Map<String, AnimalShelter> shelters;

    public ShelterManager() {
        this.shelters = new HashMap<>();
    }

    public AnimalShelter getShelter(String name) {
        return shelters.get(name);
    }

    public boolean addShelter(String name, int maxCapacity) {
        if (shelters.containsKey(name)) {
            System.err.println("Schronisko o tej nazwie już istnieje.");
            return false;
        }
        AnimalShelter newShelter = new AnimalShelter(name, maxCapacity);

        shelters.put(name, newShelter);
        System.out.println("Dodano schronisko: " + newShelter.getShelterName() + " z pojemnością: " + newShelter.getMaxCapacity());

        return true;
    }

    public boolean removeShelter(String name) {
        if (shelters.containsKey(name)) {
            shelters.remove(name);
            System.out.println("Usunięto schronisko: " + name);

            return true;
        } else {
            System.err.println("Nie znaleziono schroniska o nazwie: " + name);

            return false;
        }
    }

    public List<String> findEmpty() {
        List<String> emptyShelters = new ArrayList<>();

        for (AnimalShelter shelter : shelters.values()) {
            if (shelter.getAnimals().isEmpty()) {
                emptyShelters.add(shelter.getShelterName());
            }
        }

        return emptyShelters;
    }

    public void summary() {
        for (AnimalShelter shelter : shelters.values()) {
            int currentCapacity = shelter.getAnimals().size();
            int maxCapacity = shelter.getMaxCapacity();
            double occupancyRate = (double) currentCapacity / maxCapacity * 100;

            System.out.printf("Schronisko: %s, Zapełnienie: %.2f%%\n", shelter.getShelterName(), occupancyRate);
        }
    }
}