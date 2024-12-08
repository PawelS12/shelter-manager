package com.example.shelterjavafx.model;

import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ratings")
public class Rating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value", nullable = false)
    private int value; // Ocena w skali 0-5

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private AnimalShelter shelter; // Schronisko, któremu wystawiono ocenę

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date; // Data wystawienia oceny

    @Column(name = "comment", nullable = false)
    private String comment; // Komentarz do oceny

    // Konstruktor domyślny
    public Rating() {}

    // Konstruktor z parametrami
    public Rating(int value, AnimalShelter shelter, Date date, String comment) {
        this.value = value;
        this.shelter = shelter;
        this.date = date;
        this.comment = comment;
    }

    // Gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public AnimalShelter getShelter() {
        return shelter;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void addRating(Session session, AnimalShelter shelter, int value, String comment) {
        Rating rating = new Rating(value, shelter, new Date(), comment);  // Tworzymy nową ocenę
        shelter.getRatings().add(rating); // Dodajemy ocenę do schroniska
        session.save(rating); // Zapisujemy ocenę w bazie danych
    }
}