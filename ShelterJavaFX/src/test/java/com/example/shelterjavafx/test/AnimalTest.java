//package com.example.shelterjavafx.test;
//
//import com.example.shelterjavafx.model.Animal;
//import com.example.shelterjavafx.model.AnimalCondition;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AnimalTest {
//
//    private Animal animal;
//    private Animal animal2;
//    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//
//    @BeforeEach
//    void setUp() {
//        animal = new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 3, 100.0);
//        animal2 = new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 3, 100.0);
//        System.setOut(new PrintStream(outputStreamCaptor));
//    }
//
//    @Test
//    void testGetName() {
//        assertEquals("Rex", animal.getName());
//    }
//
//    @Test
//    void testSetName() {
//        animal.setName("Buddy");
//        assertEquals("Buddy", animal.getName());
//    }
//
//    @Test
//    void testGetSpecies() {
//        assertEquals("Dog", animal.getSpecies());
//    }
//
//    @Test
//    void testSetSpecies() {
//        animal.setSpecies("Cat");
//        assertEquals("Cat", animal.getSpecies());
//    }
//
//    @Test
//    void testGetAge() {
//        assertEquals(3, animal.getAge());
//    }
//
//    @Test
//    void testSetAge() {
//        animal.setAge(5);
//        assertEquals(5, animal.getAge());
//    }
//
//    @Test
//    void testGetPrice() {
//        assertEquals(100.0, animal.getPrice());
//    }
//
//    @Test
//    void testSetPrice() {
//        animal.setPrice(150.0);
//        assertEquals(150.0, animal.getPrice());
//    }
//
//    @Test
//    void testIsAdopted() {
//        assertFalse(animal.isAdopted());
//    }
//
//    @Test
//    void testSetAdopted() {
//        animal.setAdopted();
//        assertTrue(animal.isAdopted());
//    }
//
//    @Test
//    void testCompareToEqualAnimals() {
//        assertEquals(0, animal.compareTo(animal2));
//    }
//
//    @Test
//    void testCompareToDifferentName() {
//        animal2.setName("Buddy");
//        assertTrue(animal.compareTo(animal2) > 0);
//    }
//
//    @Test
//    void testCompareToDifferentSpecies() {
//        animal2.setSpecies("Cat");
//        assertTrue(animal.compareTo(animal2) > 0);
//    }
//
//    @Test
//    void testCompareToDifferentAge() {
//        animal2.setAge(5);
//        assertTrue(animal.compareTo(animal2) < 0);
//    }
//
//    @Test
//    void testCompareToNull() {
//        assertTrue(animal.compareTo(null) > 0);
//    }
//
//    @Test
//    void testPrint() {
//        animal.Print();
//        String output = outputStreamCaptor.toString();
//        assertTrue(output.contains("Imię: Rex"));
//        assertTrue(output.contains("Gatunek: Dog"));
//        assertTrue(output.contains("Stan: HEALTHY"));
//        assertTrue(output.contains("Wiek: 3"));
//        assertTrue(output.contains("Cena: 100.0"));
//    }
//}