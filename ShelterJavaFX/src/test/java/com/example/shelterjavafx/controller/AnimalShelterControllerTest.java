package com.example.shelterjavafx.controller;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.model.AnimalShelter;
import com.example.shelterjavafx.service.AnimalShelterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AnimalShelterController.class)
class AnimalShelterControllerTest {

    @Mock
    private AnimalShelterService animalShelterService;

    @InjectMocks
    private AnimalShelterController animalShelterController;

    private AnimalShelter shelter;
    private Animal animal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Przykładowe dane testowe
        shelter = new AnimalShelter();
        shelter.setId(1L);
        shelter.setShelterName("Test Shelter");
        shelter.setMaxCapacity(50);

        animal = new Animal();
        animal.setId(1L);
        animal.setName("Dog");
        animal.setSpecies("Dog");
    }

    // Test: POST /api/animalshelter - Dodanie nowego schroniska
    @Test
    void shouldReturnCreatedStatusWhenShelterIsAdded() {
        when(animalShelterService.save(any(AnimalShelter.class))).thenReturn(shelter);

        ResponseEntity<AnimalShelter> response = animalShelterController.addShelter(shelter);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Shelter", response.getBody().getShelterName());
        verify(animalShelterService, times(1)).save(any(AnimalShelter.class));
    }

    // Test: DELETE /api/animalshelter/{id} - Usunięcie schroniska
    @Test
    void shouldReturnNoContentWhenShelterIsDeletedSuccessfully() {
        when(animalShelterService.delete(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = animalShelterController.deleteShelter(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(animalShelterService, times(1)).delete(1L);
    }

    @Test
    void shouldReturnNotFoundWhenShelterToDeleteDoesNotExist() {
        when(animalShelterService.delete(anyLong())).thenReturn(false);

        ResponseEntity<Void> response = animalShelterController.deleteShelter(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(animalShelterService, times(1)).delete(1L);
    }

    // Test: GET /api/animalshelter/{id}/csv - Pobranie danych w formacie CSV
    @Test
    void shouldReturnCSVFileWhenExportIsSuccessful() throws IOException {
        String filename = "shelter_1.csv";
        byte[] mockFileContent = "id,name,species".getBytes();

        doNothing().when(animalShelterService).exportShelterToCSV(anyLong(), anyString());
        when(Files.readAllBytes(any())).thenReturn(mockFileContent);

        ResponseEntity<byte[]> response = animalShelterController.exportShelterToCSV(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertArrayEquals(mockFileContent, response.getBody());
        assertEquals("attachment; filename=shelter_1.csv", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        verify(animalShelterService, times(1)).exportShelterToCSV(1L, filename);
    }

    @Test
    void shouldReturnInternalServerErrorWhenCSVExportFails() throws IOException {
        doThrow(IOException.class).when(animalShelterService).exportShelterToCSV(anyLong(), anyString());

        ResponseEntity<byte[]> response = animalShelterController.exportShelterToCSV(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Test: GET /api/animalshelter/{id}/animals - Pobranie zwierząt w schronisku
    @Test
    void shouldReturnAnimalsWhenShelterHasAnimals() {
        when(animalShelterService.getAnimalsInShelter(anyLong())).thenReturn(List.of(animal));

        ResponseEntity<List<Animal>> response = animalShelterController.getAnimalsInShelter(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Dog", response.getBody().get(0).getName());
        verify(animalShelterService, times(1)).getAnimalsInShelter(1L);
    }

    @Test
    void shouldReturnNoContentWhenShelterHasNoAnimals() {
        when(animalShelterService.getAnimalsInShelter(anyLong())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Animal>> response = animalShelterController.getAnimalsInShelter(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(animalShelterService, times(1)).getAnimalsInShelter(1L);
    }

    // Test: GET /api/animalshelter/{id}/fill - Sprawdzenie zapełnienia schroniska
    @Test
    void shouldReturnFillStatusWhenShelterExists() {
        when(animalShelterService.getShelterFill(anyLong())).thenReturn("50%");

        ResponseEntity<String> response = animalShelterController.getShelterFill(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("50%", response.getBody());
        verify(animalShelterService, times(1)).getShelterFill(1L);
    }

    @Test
    void shouldReturnInternalServerErrorWhenShelterFillFails() {
        when(animalShelterService.getShelterFill(anyLong())).thenThrow(RuntimeException.class);

        ResponseEntity<String> response = animalShelterController.getShelterFill(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}