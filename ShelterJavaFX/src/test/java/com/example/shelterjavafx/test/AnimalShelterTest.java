package com.example.shelterjavafx.test;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.AnimalCondition;
import com.example.shelterjavafx.model.AnimalShelter;
import com.example.shelterjavafx.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalShelterTest {

    private AnimalShelter shelter;
    private Animal dog;
    private Animal cat;

    @BeforeEach
    void setUp() {
        shelter = new AnimalShelter("Shelter A", 5);
        dog = new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 3, 100.0);
        cat = new Animal("Mia", "Cat", AnimalCondition.SICK, 2, 50.0);
    }

    @Test
    void testGetMaxCapacity() {
        assertEquals(5, shelter.getMaxCapacity(), "Initial max capacity should be 5");
    }

    @Test
    void testSetMaxCapacity() {
        shelter.setMaxCapacity(10);
        assertEquals(10, shelter.getMaxCapacity(), "Max capacity should be updated to 10");
    }

    @Test
    void testGetShelterName() {
        assertEquals("Shelter A", shelter.getShelterName(), "Initial shelter name should be 'Shelter A'");
    }

    @Test
    void testSetShelterName() {
        shelter.setShelterName("New Shelter");
        assertEquals("New Shelter", shelter.getShelterName(), "Shelter name should be updated to 'New Shelter'");
    }

    @Test
    void testGetAnimals() {
        assertTrue(shelter.getAnimals().isEmpty(), "Initial animal list should be empty");
        Animal dog = new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 3, 100.0);
        shelter.addAnimal(dog);
        assertEquals(1, shelter.getAnimals().size(), "Animal list should contain 1 animal after adding");
        assertTrue(shelter.getAnimals().contains(dog), "Animal list should contain the added animal");
    }

    @Test
    void testGetCurrentAnimals() {
        assertEquals(0, shelter.getCurrentAnimals(), "Initial number of animals should be 0");
        Animal dog = new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 3, 100.0);
        shelter.addAnimal(dog);
        assertEquals(1, shelter.getCurrentAnimals(), "Current number of animals should be 1 after adding an animal");
    }

    @Test
    void testAddAnimal() {
        assertTrue(shelter.addAnimal(dog));
        assertEquals(1, shelter.getCurrentAnimals());
        assertTrue(shelter.getAnimals().contains(dog));
    }

    @Test
    void testAddAnimalExceedingCapacity() {
        shelter.addAnimal(dog);
        shelter.addAnimal(new Animal("Benny", "Rabbit", AnimalCondition.ADOPTION, 1, 80.0));
        shelter.addAnimal(new Animal("Bella", "Dog", AnimalCondition.SICK, 4, 120.0));
        shelter.addAnimal(new Animal("Fido", "Dog", AnimalCondition.HEALTHY, 2, 150.0));
        shelter.addAnimal(new Animal("Lucy", "Cat", AnimalCondition.HEALTHY, 2, 70.0));
        assertFalse(shelter.addAnimal(new Animal("Max", "Dog", AnimalCondition.QUARANTINE, 3, 130.0)));
    }

    @Test
    void testRemoveAnimal() {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        assertTrue(shelter.removeAnimal(dog));
        assertEquals(1, shelter.getCurrentAnimals());
        assertFalse(shelter.getAnimals().contains(dog));
    }

    @Test
    void testAdoptAnimal() {
        Student student = new Student("Pawel", "Socala");
        shelter.addAnimal(dog);
        assertTrue(shelter.adoptAnimal(dog, student));
        assertTrue(student.getAnimals().contains(dog));
        assertFalse(shelter.getAnimals().contains(dog));
    }

    @Test
    void testChangeCondition() {
        shelter.addAnimal(dog);
        assertTrue(shelter.changeCondition(dog, AnimalCondition.SICK));
        assertEquals(AnimalCondition.SICK, dog.getCondition());
    }

    @Test
    void testChangeAge() {
        shelter.addAnimal(dog);
        assertTrue(shelter.changeAge(dog, 4));
        assertEquals(4, dog.getAge());
    }

    @Test
    void testCountByCondition() {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        assertEquals(1, shelter.countByCondition(AnimalCondition.HEALTHY));
        assertEquals(1, shelter.countByCondition(AnimalCondition.SICK));
    }

    @Test
    void testSortByName() {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        List<Animal> sortedAnimals = shelter.sortByName();
        assertEquals("Mia", sortedAnimals.get(0).getName());
        assertEquals("Rex", sortedAnimals.get(1).getName());
    }

    @Test
    void testSortByPrice() {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        List<Animal> sortedAnimals = shelter.sortByPrice();
        assertEquals(50.0, sortedAnimals.get(0).getPrice());
        assertEquals(100.0, sortedAnimals.get(1).getPrice());
    }

    @Test
    void testSearchByName() {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        assertEquals("Rex", shelter.search("Rex"));
        assertNull(shelter.search("Max"));
    }

    @Test
    void testSearchPartial() {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        List<Animal> matchingAnimals = shelter.searchPartial("Re");
        assertTrue(matchingAnimals.contains(dog));
        assertFalse(matchingAnimals.contains(cat));
    }

    @Test
    void testSummary() {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        shelter.summary();
    }

    @Test
    void testMax() {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        Animal maxPriceAnimal = shelter.max();
        assertEquals(dog, maxPriceAnimal);
    }

    @Test
    void testSortSheltersByMaxCapacity() {
        AnimalShelter shelter1 = new AnimalShelter("Small Shelter", 5);
        AnimalShelter shelter2 = new AnimalShelter("Medium Shelter", 10);
        AnimalShelter shelter3 = new AnimalShelter("Large Shelter", 15);

        List<AnimalShelter> shelters = new ArrayList<>();
        shelters.add(shelter2);
        shelters.add(shelter3);
        shelters.add(shelter1);

        List<AnimalShelter> sortedShelters = AnimalShelter.sortSheltersByMaxCapacity(shelters);
        assertEquals(5, sortedShelters.get(0).getMaxCapacity());
        assertEquals("Small Shelter", sortedShelters.get(0).getShelterName());
        assertEquals(10, sortedShelters.get(1).getMaxCapacity());
        assertEquals("Medium Shelter", sortedShelters.get(1).getShelterName());
        assertEquals(15, sortedShelters.get(2).getMaxCapacity());
        assertEquals("Large Shelter", sortedShelters.get(2).getShelterName());
    }
}