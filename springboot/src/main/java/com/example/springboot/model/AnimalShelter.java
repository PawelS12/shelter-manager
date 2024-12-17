package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shelters")
public class AnimalShelter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shelter_name", nullable = false, unique = true)
    private String shelterName;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Animal> animals = new ArrayList<>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Rating> ratings = new ArrayList<>();

    private int numberOfAnimals;
    public AnimalShelter() {}

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = shelterName;
        this.maxCapacity = maxCapacity;
    }

    public Long getId() {
        return id;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public boolean addAnimal(Animal animal) {
        if (animals.size() >= maxCapacity) {
            return false;
        }
        if (animals.stream().anyMatch(a -> a.getName().equalsIgnoreCase(animal.getName()))) {
            return false;
        }
        animal.setShelter(this);
        animals.add(animal);
        return true;
    }

    public boolean removeAnimal(Animal animal) {
        return animals.removeIf(existingAnimal -> existingAnimal.getId().equals(animal.getId()));
    }

    public String getFormattedAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return "No ratings (0)";
        }
        double sum = ratings.stream().mapToDouble(Rating::getValue).sum();
        double average = sum / ratings.size();
        return String.format("%.1f (%d)", average, ratings.size());
    }

    public String getFillStatus() {
        return String.format("%d/%d", animals.size(), maxCapacity);
    }
}