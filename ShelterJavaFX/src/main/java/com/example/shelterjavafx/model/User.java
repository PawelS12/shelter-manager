package com.example.shelterjavafx.model;

import org.hibernate.Session;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_animals",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "animal_id")
    )
    private List<Animal> animals;

    public User() {
        this.animals = new ArrayList<>();
    }

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.animals = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public boolean adoptAnimal(Animal animal, Session session) {
        if (animal != null && animal.getCondition() != AnimalCondition.ADOPTION) {
            animal.setCondition(AnimalCondition.ADOPTION);

            this.animals.add(animal);

            session.beginTransaction();
            session.saveOrUpdate(animal);
            session.saveOrUpdate(this);
            session.getTransaction().commit();

            return true;
        }
        return false;
    }

    public void displayAnimals() {
        for (Animal a : animals) {
            System.out.println(a);
        }
    }
}