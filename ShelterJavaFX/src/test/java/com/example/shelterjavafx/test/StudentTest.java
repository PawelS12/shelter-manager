package com.example.shelterjavafx.test;
import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.User;
import com.example.shelterjavafx.model.AnimalCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private User student;
    private Animal animal;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        student = new User("Jan", "Kowalski");
        animal = new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 5, 200.0);
    }

    @Test
    void testConstructor() {
        assertNotNull(student.getAnimals(), "Animals list should not be null.");
        assertTrue(student.getAnimals().isEmpty(), "Animals list should be empty initially.");
    }

    @Test
    void testAddAnimal() {
        student.getAnimals().add(animal);
        assertFalse(student.getAnimals().isEmpty(), "Animals list should not be empty.");
        assertEquals(1, student.getAnimals().size(), "Animals list should contain 1 animal.");
        assertEquals(animal, student.getAnimals().get(0), "The animal in the list should be the one added.");
    }
}