package com.example.springboot.repository;

import com.example.springboot.model.AnimalShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalShelterRepository extends JpaRepository<AnimalShelter, Long> {
    List<AnimalShelter> findByMaxCapacity(int maxCapacity);
    Optional<AnimalShelter> findByShelterName(String shelterName);

    @Query("SELECT a FROM AnimalShelter a WHERE a.numberOfAnimals > 0")
    List<AnimalShelter> findFullShelters();
}