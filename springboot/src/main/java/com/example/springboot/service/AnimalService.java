package com.example.springboot.service;

import com.example.springboot.model.Animal;
import com.example.springboot.repository.AnimalRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public Animal save(Animal animal) {
        return animalRepository.save(animal);
    }

    public boolean deleteById(Long id) {
        Optional<Animal> animal = animalRepository.findById(id);
        if (animal.isPresent()) {
            animalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Animal findById(Long id) {
        return animalRepository.findById(id).orElse(null);
    }

    public List<Animal> findAll() {
        return animalRepository.findAll();
    }

    public List<Animal> findByShelterId(Long shelterId) {
        return animalRepository.findByShelterId(shelterId);
    }

    public Animal update(Long id, Animal updatedAnimal) {
        Optional<Animal> existingAnimalOpt = animalRepository.findById(id);
        if (existingAnimalOpt.isPresent()) {
            Animal existingAnimal = existingAnimalOpt.get();
            existingAnimal.setName(updatedAnimal.getName());
            existingAnimal.setSpecies(updatedAnimal.getSpecies());
            existingAnimal.setCondition(updatedAnimal.getCondition());
            existingAnimal.setAge(updatedAnimal.getAge());
            existingAnimal.setPrice(updatedAnimal.getPrice());
            existingAnimal.setAdopted(updatedAnimal.isAdopted());
            existingAnimal.setShelter(updatedAnimal.getShelter());
            return animalRepository.save(existingAnimal);
        }
        return null;
    }
}
