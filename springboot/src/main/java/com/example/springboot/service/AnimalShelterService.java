package com.example.springboot.service;

import com.example.springboot.model.Animal;
import com.example.springboot.model.AnimalShelter;
import com.example.springboot.repository.AnimalShelterRepository;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalShelterService {
    private final AnimalShelterRepository shelterRepository;

    public AnimalShelterService(AnimalShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public AnimalShelter save(AnimalShelter shelter) {
        return shelterRepository.save(shelter);
    }

    public boolean delete(Long id) {
        if (shelterRepository.existsById(id)) {
            shelterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public AnimalShelter findById(Long id) {
        Optional<AnimalShelter> shelter = shelterRepository.findById(id);
        return shelter.orElse(null);
    }

    public List<AnimalShelter> findAll() {
        return shelterRepository.findAll();
    }

    public List<Animal> getAnimalsInShelter(Long shelterId) {
        AnimalShelter shelter = shelterRepository.findById(shelterId).orElse(null);
        return shelter != null ? shelter.getAnimals() : null;
    }

    public Animal addAnimalToShelter(Long shelterId, Animal animal) {
        AnimalShelter shelter = shelterRepository.findById(shelterId).orElse(null);
        if (shelter != null) {
            if (shelter.addAnimal(animal)) {
                return animal;
            }
        }
        return null;
    }

    public boolean removeAnimalFromShelter(Long shelterId, Long animalId) {
        AnimalShelter shelter = shelterRepository.findById(shelterId).orElse(null);
        if (shelter != null) {
            Animal animal = new Animal();
            animal.setId(animalId);
            return shelter.removeAnimal(animal);
        }
        return false;
    }

    public String getShelterFill(Long shelterId) {
        AnimalShelter shelter = shelterRepository.findById(shelterId).orElse(null);
        if (shelter != null) {
            return "Shelter is " + shelter.getAnimals().size() + "/" + shelter.getMaxCapacity();
        }
        return "Shelter not found";
    }

    public void exportShelterToCSV(Long shelterId, String filename) throws IOException {
        AnimalShelter shelter = shelterRepository.findById(shelterId).orElse(null);

        if (shelter != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write("Shelter Name,Max Capacity\n");
                writer.write(shelter.getShelterName() + "," + shelter.getMaxCapacity() + "\n");
                writer.write("ID,Name,Species,Condition,Age,Price,Is Adopted\n");
                List<Animal> animals = shelter.getAnimals();

                for (Animal animal : animals) {
                    writer.write(animal.getId() + "," +
                            animal.getName() + "," +
                            animal.getSpecies() + "," +
                            animal.getCondition() + "," +
                            animal.getAge() + "," +
                            animal.getPrice() + "," +
                            animal.isAdopted() + "\n");
                }
            }
            System.out.println("Data exported successfully to: " + filename);
        } else {
            throw new IOException("Shelter with ID " + shelterId + " not found.");
        }
    }
}