package com.example.springboot.service;

import com.example.springboot.model.AnimalShelter;
import com.example.springboot.repository.AnimalShelterRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShelterManagerService {

    private final AnimalShelterRepository shelterRepository;

    public ShelterManagerService(AnimalShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public List<AnimalShelter> getAllShelters() {
        return shelterRepository.findAll();
    }

    public AnimalShelter addShelter(String name, int maxCapacity) {
        AnimalShelter shelter = new AnimalShelter(name, maxCapacity);
        return shelterRepository.save(shelter);
    }

    public boolean removeShelter(Long shelterId) {
        Optional<AnimalShelter> shelter = shelterRepository.findById(shelterId);
        if (shelter.isPresent()) {
            shelterRepository.delete(shelter.get());
            return true;
        }
        return false;
    }

    public List<AnimalShelter> getEmptyShelters() {
        return shelterRepository.findByMaxCapacity(0);
    }

    public String getShelterSummary() {
        List<AnimalShelter> shelters = shelterRepository.findAll();
        long emptySheltersCount = shelters.stream().filter(shelter -> shelter.getMaxCapacity() == 0).count();
        long fullSheltersCount = shelters.stream().filter(shelter -> shelter.getMaxCapacity() > 0).count();
        return String.format("Total Shelters: %d, Empty Shelters: %d, Full Shelters: %d", shelters.size(), emptySheltersCount, fullSheltersCount);
    }
}