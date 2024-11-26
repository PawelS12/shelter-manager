package com.example.shelterjavafx.test;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.AnimalCondition;
import com.example.shelterjavafx.model.ShelterManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShelterManagerTest {
    private ShelterManager shelterManager;
    private Animal shelterAnimal;

    @BeforeEach
    void setUp() {
        shelterManager = new ShelterManager();
    }

    @Test
    void testAddShelter() {
        boolean result = shelterManager.addShelter("Shelter A", 10);
        assertTrue(result);
        assertNotNull(shelterManager.getShelter("Shelter A"));
        result = shelterManager.addShelter("Shelter A", 15);
        assertFalse(result);
    }

    @Test
    void testRemoveShelter() {
        shelterManager.addShelter("Shelter A", 10);
        boolean result = shelterManager.removeShelter("Shelter A");
        assertTrue(result);
        assertNull(shelterManager.getShelter("Shelter A"));
        result = shelterManager.removeShelter("Nonexistent Shelter");
        assertFalse(result);
    }

    @Test
    void testFindEmpty() {
        shelterManager.addShelter("Shelter A", 10);
        shelterManager.addShelter("Shelter B", 10);

        List<String> emptyShelters = shelterManager.findEmpty();
        assertEquals(2, emptyShelters.size());
        assertTrue(emptyShelters.contains("Shelter A"));
        assertTrue(emptyShelters.contains("Shelter B"));

        Animal shelterAnimal = new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 3, 100.0);
        shelterManager.getShelter("Shelter A").addAnimal(shelterAnimal);

        emptyShelters = shelterManager.findEmpty();
        assertEquals(1, emptyShelters.size());
        assertTrue(emptyShelters.contains("Shelter B"));
    }
}