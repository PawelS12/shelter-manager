package com.example.shelterjavafx.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class AnimalTableModel {

    private final ObservableList<Animal> animals;

    public AnimalTableModel(List<Animal> animals) {
        this.animals = FXCollections.observableArrayList(animals);
    }

    public ObservableList<Animal> getAnimals() {
        return animals;
    }

    public static class Animal {
        private final StringProperty name;
        private final StringProperty species;
        private final StringProperty condition;
        private final IntegerProperty age;
        private final DoubleProperty price;
        private final BooleanProperty adopted;

        public Animal(String name, String species, String condition, int age, double price, boolean adopted) {
            this.name = new SimpleStringProperty(name);
            this.species = new SimpleStringProperty(species);
            this.condition = new SimpleStringProperty(condition);
            this.age = new SimpleIntegerProperty(age);
            this.price = new SimpleDoubleProperty(price);
            this.adopted = new SimpleBooleanProperty(adopted);
        }
    }
}