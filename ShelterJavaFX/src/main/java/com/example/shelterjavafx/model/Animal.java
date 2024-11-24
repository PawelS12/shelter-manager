package com.example.shelterjavafx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Animal implements Comparable<Animal> {
    private final StringProperty name;
    private final StringProperty species;
    private AnimalCondition condition;
    private final IntegerProperty age;
    private final DoubleProperty price;
    private final BooleanProperty isAdopted;

    public Animal(String name, String species, AnimalCondition condition, int age, double price) {
        this.name = new SimpleStringProperty(name);
        this.species = new SimpleStringProperty(species);
        this.condition = condition;
        this.age = new SimpleIntegerProperty(age);
        this.price = new SimpleDoubleProperty(price);
        this.isAdopted = new SimpleBooleanProperty(false);
    }

    // Settery i gettery z właściwościami (binding properties)

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty speciesProperty() {
        return species;
    }

    public String getSpecies() {
        return species.get();
    }

    public void setSpecies(String species) {
        this.species.set(species);
    }

    public AnimalCondition getCondition() {
        return condition;
    }

    public void setCondition(AnimalCondition condition) {
        this.condition = condition;
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public BooleanProperty isAdoptedProperty() {
        return isAdopted;
    }

    public boolean isAdopted() {
        return isAdopted.get();
    }

    public void setAdopted() {
        this.isAdopted.set(true);
    }

    // Print method (for debugging/logging)
    public void Print() {
        System.out.println("Imię: " + name.get() + " | Gatunek: " + species.get() + " | Stan: " + condition + " | Wiek: " + age.get() + " | Cena: " + price.get());
    }

    @Override
    public int compareTo(Animal other) {
        if (other == null) {
            return 1;
        }

        int nameComparison = this.name.get().compareTo(other.name.get());
        if (nameComparison != 0) {
            return nameComparison;
        }

        int speciesComparison = this.species.get().compareTo(other.species.get());
        if (speciesComparison != 0) {
            return speciesComparison;
        }

        return Integer.compare(this.age.get(), other.age.get());
    }
}
