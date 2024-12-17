package com.example.springboot.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.Session;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ratings")
public class Rating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value", nullable = false)
    private int value;

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    @JsonBackReference
    private AnimalShelter shelter;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "comment", nullable = false)
    private String comment;

    public Rating() {}

    public Rating(int value, AnimalShelter shelter, Date date, String comment) {
        this.value = value;
        this.shelter = shelter;
        this.date = date;
        this.comment = comment;
    }

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
        Rating rating = new Rating(value, shelter, new Date(), comment);
        shelter.getRatings().add(rating);
        session.save(rating);
    }
}