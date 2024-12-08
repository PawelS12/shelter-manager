package com.example.shelterjavafx.model;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ShelterManager {

    public ShelterManager() {
        // Konstruktor bez dodatkowych danych, korzystamy z Hibernate do zarządzania
    }

    public AnimalShelter getShelter(String name, Session session) {
        Query<AnimalShelter> query = session.createQuery("FROM AnimalShelter s WHERE s.shelterName = :name", AnimalShelter.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }

    public boolean addShelter(String name, int maxCapacity, Session session) {
        if (getShelter(name, session) != null) {
            System.err.println("Schronisko o tej nazwie już istnieje.");
            return false;
        }
        AnimalShelter newShelter = new AnimalShelter(name, maxCapacity);
        session.save(newShelter);
        System.out.println("Dodano schronisko: " + newShelter.getShelterName() + " z pojemnością: " + newShelter.getMaxCapacity());
        return true;
    }

    public boolean removeShelter(String name, Session session) {
        AnimalShelter shelter = getShelter(name, session);
        if (shelter != null) {
            session.delete(shelter);
            System.out.println("Usunięto schronisko: " + name);
            return true;
        } else {
            System.err.println("Nie znaleziono schroniska o nazwie: " + name);
            return false;
        }
    }

    public List<String> findEmpty(Session session) {
        Query<String> query = session.createQuery(
                "SELECT s.shelterName FROM AnimalShelter s WHERE size(s.animals) = 0", String.class);
        return query.list();
    }

    public void summary(Session session) {
        Query<AnimalShelter> query = session.createQuery("FROM AnimalShelter", AnimalShelter.class);
        List<AnimalShelter> shelters = query.list();

        for (AnimalShelter shelter : shelters) {
            int currentCapacity = shelter.getAnimals().size();
            int maxCapacity = shelter.getMaxCapacity();
            double occupancyRate = (double) currentCapacity / maxCapacity * 100;

            System.out.printf("Schronisko: %s, Zapełnienie: %.2f%%%n", shelter.getShelterName(), occupancyRate);
        }
    }
}
