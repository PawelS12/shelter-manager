package com.example.shelterjavafx.data;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.AnimalCondition;
import com.example.shelterjavafx.model.AnimalShelter;
import com.example.shelterjavafx.model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.*;
import java.util.List;

public class ShelterDataCSV {

    public static void exportAllSheltersToCSV(String filename) throws IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            String hqlShelters = "FROM AnimalShelter AS shelter LEFT JOIN FETCH shelter.animals";
            Query<AnimalShelter> shelterQuery = session.createQuery(hqlShelters, AnimalShelter.class);
            List<AnimalShelter> shelters = shelterQuery.list();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

                writer.write("Shelter Name,Max Capacity\n");

                for (AnimalShelter shelter : shelters) {
                    writer.write(shelter.getShelterName() + "," + shelter.getMaxCapacity() + "\n");
                    List<Animal> animals = shelter.getAnimals();
                    writer.write("ID,Name,Species,Condition,Age,Price,Is Adopted\n");

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
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            transaction = session.beginTransaction();

            String line = reader.readLine();
            String shelterLine = reader.readLine();
            String[] shelterData = shelterLine.split(",");
            String shelterName = shelterData[0];
            int maxCapacity = Integer.parseInt(shelterData[1]);

            AnimalShelter shelter = new AnimalShelter();
            shelter.setShelterName(shelterName);
            shelter.setMaxCapacity(maxCapacity);

            session.persist(shelter);

            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] animalData = line.split(",");

                String name = animalData[1];
                String species = animalData[2];
                AnimalCondition condition = AnimalCondition.valueOf(animalData[3].toUpperCase());
                int age = Integer.parseInt(animalData[4]);
                double price = Double.parseDouble(animalData[5]);
                boolean isAdopted = Boolean.parseBoolean(animalData[6]);

                Animal animal = new Animal();
                animal.setName(name);
                animal.setSpecies(species);
                animal.setCondition(condition);
                animal.setAge(age);
                animal.setPrice(price);
                animal.setAdopted(isAdopted);
                animal.setShelter(shelter);

                session.persist(animal);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IOException("Błąd podczas importowania danych z CSV: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public static void exportSheltersToBinary(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                String hqlShelters = "FROM AnimalShelter AS shelter LEFT JOIN FETCH shelter.animals";
                Query<AnimalShelter> shelterQuery = session.createQuery(hqlShelters, AnimalShelter.class);
                List<AnimalShelter> shelters = shelterQuery.list();
                oos.writeObject(shelters);
            } finally {
                session.close();
            }
        }
    }

    public static List<AnimalShelter> importSheltersFromBinary(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<AnimalShelter>) ois.readObject();
        }
    }
}