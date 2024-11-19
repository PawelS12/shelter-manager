package com.example.shelterjavafx.model;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private String surname;
    private List<Animal> animals;

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.animals = new ArrayList<Animal>();
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void displayAnimals() {
        for (Animal a : animals) {
            a.Print();
        }
    }
}