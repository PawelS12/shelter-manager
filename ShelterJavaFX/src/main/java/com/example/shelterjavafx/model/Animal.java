package com.example.shelterjavafx.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "animals")
public class Animal implements Comparable<Animal>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Column(name = "animal_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalCondition condition;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private double price;

    @Column(name = "is_adopted", nullable = false)
    private boolean isAdopted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = false)
    private AnimalShelter shelter;

    // Default constructor required by JPA
    public Animal() {
    }

    // Constructor for creating a new Animal instance
    public Animal(String name, String species, AnimalCondition condition, int age, double price) {
        this.name = name;
        this.species = species;
        this.condition = condition;
        this.age = age;
        this.price = price;
        this.isAdopted = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long Id) {
        this.id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public AnimalCondition getCondition() {
        return condition;
    }

    public void setCondition(AnimalCondition condition) {
        this.condition = condition;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAdopted() {
        return isAdopted;
    }

    public void setAdopted(boolean adopted) {
        isAdopted = adopted;
    }

    public AnimalShelter getShelter() {
        return shelter;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    // Comparable implementation to sort by name, species, and age
    @Override
    public int compareTo(Animal other) {
        if (other == null) {
            return 1;
        }
        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }
        int speciesComparison = this.species.compareTo(other.species);
        if (speciesComparison != 0) {
            return speciesComparison;
        }
        return Integer.compare(this.age, other.age);
    }

    // Override toString for better logging and debugging
    @Override
    public String toString() {
        return String.format(
                "Animal{id=%d, name='%s', species='%s', condition='%s', age=%d, price=%.2f, isAdopted=%b, shelterId=%d}",
                id, name, species, condition, age, price, isAdopted, shelter != null ? shelter.getId() : null);
    }
}