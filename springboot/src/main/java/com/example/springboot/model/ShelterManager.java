package com.example.springboot.model;

import com.example.springboot.repository.AnimalShelterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelterManager {

    private final AnimalShelterRepository shelterRepository;

    public ShelterManager(AnimalShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public AnimalShelter getShelter(String name) {
        return shelterRepository.findByShelterName(name).orElse(null);
    }

    public boolean addShelter(String name, int maxCapacity) {
        if (shelterRepository.findByShelterName(name).isPresent()) {
            System.err.println("Schronisko o tej nazwie już istnieje.");
            return false;
        }
        AnimalShelter newShelter = new AnimalShelter(name, maxCapacity);
        shelterRepository.save(newShelter);
        System.out.println("Dodano schronisko: " + newShelter.getShelterName() + " z pojemnością: " + newShelter.getMaxCapacity());
        return true;
    }

    public boolean removeShelter(String name) {
        AnimalShelter shelter = getShelter(name);
        if (shelter != null) {
            shelterRepository.delete(shelter);
            System.out.println("Usunięto schronisko: " + name);
            return true;
        } else {
            System.err.println("Nie znaleziono schroniska o nazwie: " + name);
            return false;
        }
    }

    public void summary() {
        List<AnimalShelter> shelters = shelterRepository.findAll();

        for (AnimalShelter shelter : shelters) {
            int currentCapacity = shelter.getAnimals().size();
            int maxCapacity = shelter.getMaxCapacity();
            double occupancyRate = (double) currentCapacity / maxCapacity * 100;

            System.out.printf("Schronisko: %s, Zapełnienie: %.2f%%%n", shelter.getShelterName(), occupancyRate);
        }
    }
}