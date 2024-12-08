package com.example.shelterjavafx.data;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.AnimalCondition;
import com.example.shelterjavafx.model.AnimalShelter;
import com.example.shelterjavafx.model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShelterDataCSV {

    public static void exportAllSheltersToCSV(String filename) throws IOException {
        // Otwarcie sesji Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            // HQL: Pobranie wszystkich schronisk wraz z ich zwierzętami
            String hqlShelters = "FROM AnimalShelter AS shelter LEFT JOIN FETCH shelter.animals";
            Query<AnimalShelter> shelterQuery = session.createQuery(hqlShelters, AnimalShelter.class);
            List<AnimalShelter> shelters = shelterQuery.list();

            // Tworzenie pliku CSV i zapisywanie danych
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

                // Zapisujemy nagłówki dla schronisk
                writer.write("Shelter Name,Max Capacity\n");

                // Zapisujemy dane o schroniskach
                for (AnimalShelter shelter : shelters) {
                    writer.write(shelter.getShelterName() + "," + shelter.getMaxCapacity() + "\n");

                    // Pobieramy zwierzęta dla danego schroniska
                    List<Animal> animals = shelter.getAnimals();

                    // Zapisujemy nagłówki dla zwierząt
                    writer.write("ID,Name,Species,Condition,Age,Price,Is Adopted\n");

                    // Zapisujemy dane o zwierzętach
                    for (Animal animal : animals) {
                        writer.write(animal.getId() + "," +
                                animal.getName() + "," +
                                animal.getSpecies() + "," +
                                animal.getCondition() + "," +
                                animal.getAge() + "," +
                                animal.getPrice() + "," +
                                animal.isAdopted() + "\n");
                    }
                }
            }

            System.out.println("Data exported successfully to: " + filename);

        } catch (Exception e) {
            throw new IOException("Error while exporting shelter data to CSV: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public static void importShelterFromCSV(String filename) throws IOException {
        // Otwórz sesję Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            transaction = session.beginTransaction(); // Rozpoczynamy transakcję

            String line = reader.readLine(); // Skip header
            String shelterLine = reader.readLine(); // Pierwsza linia z danymi schroniska
            String[] shelterData = shelterLine.split(",");
            String shelterName = shelterData[0];
            int maxCapacity = Integer.parseInt(shelterData[1]);

            // Tworzymy i zapisujemy schronisko
            AnimalShelter shelter = new AnimalShelter();
            shelter.setShelterName(shelterName);
            shelter.setMaxCapacity(maxCapacity);

            session.persist(shelter); // Zapisujemy schronisko w bazie danych

            reader.readLine(); // Skip animal headers (id,name,species,condition,age,price,isAdopted)
            while ((line = reader.readLine()) != null) {
                String[] animalData = line.split(",");

                String name = animalData[1];
                String species = animalData[2];
                AnimalCondition condition = AnimalCondition.valueOf(animalData[3].toUpperCase()); // Mapowanie na enum
                int age = Integer.parseInt(animalData[4]);
                double price = Double.parseDouble(animalData[5]);
                boolean isAdopted = Boolean.parseBoolean(animalData[6]);

                // Tworzymy i zapisujemy zwierzę
                Animal animal = new Animal();
                animal.setName(name);
                animal.setSpecies(species);
                animal.setCondition(condition);
                animal.setAge(age);
                animal.setPrice(price);
                animal.setAdopted(isAdopted);
                animal.setShelter(shelter); // Ustawiamy powiązanie ze schroniskiem

                session.persist(animal); // Zapisujemy zwierzę w bazie danych
            }

            transaction.commit(); // Zatwierdzamy transakcję
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Wycofujemy transakcję w razie błędu
            }
            throw new IOException("Błąd podczas importowania danych z CSV: " + e.getMessage(), e);
        } finally {
            session.close(); // Zawsze zamykamy sesję
        }
    }


}
