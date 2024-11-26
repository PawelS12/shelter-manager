package com.example.shelterjavafx.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ShelterTableModel {
    private final ObservableList<AnimalShelter> shelters;

    public ShelterTableModel(List<AnimalShelter> data) {
        this.shelters = FXCollections.observableArrayList(data);
    }

    public ObservableList<AnimalShelter> getShelters() {
        return shelters;
    }

    public static class AnimalShelter {
        private final StringProperty shelterName;
        private final IntegerProperty maxCapacity;
        private final IntegerProperty currentAnimals;

        public AnimalShelter(String shelterName, int maxCapacity) {
            this.shelterName = new SimpleStringProperty(shelterName);
            this.maxCapacity = new SimpleIntegerProperty(maxCapacity);
            this.currentAnimals = new SimpleIntegerProperty(0);
        }

    }
}