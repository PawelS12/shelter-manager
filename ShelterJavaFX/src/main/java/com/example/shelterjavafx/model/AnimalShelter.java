package com.example.shelterjavafx.model;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
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
    private List<Animal> animals = new ArrayList<>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Rating> ratings = new ArrayList<>();

    public AnimalShelter() {}

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = shelterName;
        this.maxCapacity = maxCapacity;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
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

    public boolean addAnimal(Animal animal, Session session) {
        if (animals.size() >= maxCapacity) {
            System.err.println("Pełna pojemność. Nie można dodać więcej zwierząt.");
            return false;
        }
        if (findAnimalByName(animal.getName(), session) != null) {
            System.err.println("To zwierzę już istnieje.");
            return false;
        }
        animal.setShelter(this);
        session.save(animal); // Zapisz zwierzę do bazy danych
        return true;
    }

    public boolean removeAnimal(Animal animal, Session session) {
        Animal toRemove = session.get(Animal.class, animal.getId());
        if (toRemove != null) {
            session.delete(toRemove); // Usuń zwierzę z bazy danych
            return true;
        }
        return false;
    }

    public Animal findAnimalByName(String name, Session session) {
        Query<Animal> query = session.createQuery("FROM Animal a WHERE a.name = :name", Animal.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }

    public void summary(Session session) {
        System.out.println("Wszystkie zwierzęta w schronisku: ");
        Query<Animal> query = session.createQuery("FROM Animal a WHERE a.shelter.id = :shelterId", Animal.class);
        query.setParameter("shelterId", this.id);
        List<Animal> animalsFromDb = query.list();
        for (Animal animal : animalsFromDb) {
            System.out.println(animal);
        }
    }

    public List<Animal> sortByName(Session session) {
        Query<Animal> query = session.createQuery(
                "FROM Animal a WHERE a.shelter.id = :shelterId ORDER BY a.name", Animal.class);
        query.setParameter("shelterId", this.id);
        return query.list();
    }

    public List<Animal> sortByPrice(Session session) {
        Query<Animal> query = session.createQuery(
                "FROM Animal a WHERE a.shelter.id = :shelterId ORDER BY a.price", Animal.class);
        query.setParameter("shelterId", this.id);
        return query.list();
    }

    public String search(String name, Session session) {
        Query<Animal> query = session.createQuery(
                "FROM Animal a WHERE a.shelter.id = :shelterId AND LOWER(a.name) = :name", Animal.class);
        query.setParameter("shelterId", this.id);
        query.setParameter("name", name.toLowerCase());
        List<Animal> result = query.list();
        return result.isEmpty() ? null : result.get(0).getName();
    }

    public List<Animal> searchPartial(String fragment, Session session) {
        Query<Animal> query = session.createQuery(
                "FROM Animal a WHERE LOWER(a.name) LIKE :fragment OR LOWER(a.species) LIKE :fragment", Animal.class);
        query.setParameter("fragment", "%" + fragment.toLowerCase() + "%");
        return query.list();
    }

    public Animal max(Session session) {
        Query<Animal> query = session.createQuery(
                "FROM Animal a ORDER BY a.price DESC", Animal.class);
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    // Nowa metoda do obliczania średniej oceny i liczby ocen
    public String getFormattedAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return "No ratings (0)";
        }

        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getValue(); // Sumujemy wartości ocen
        }

        double average = sum / ratings.size(); // Obliczamy średnią
        return String.format("%.1f (%d)", average, ratings.size()); // Zwracamy wynik w formacie "średnia (ilość ocen)"
    }
}