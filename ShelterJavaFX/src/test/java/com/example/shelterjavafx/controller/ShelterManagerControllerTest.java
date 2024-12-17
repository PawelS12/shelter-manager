package com.example.shelterjavafx.controller;

import com.example.shelterjavafx.model.AnimalShelter;
import com.example.shelterjavafx.service.ShelterManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ShelterManagerController.class)
class ShelterManagerControllerTest {

    @Mock
    private ShelterManagerService shelterManagerService;

    @InjectMocks
    private ShelterManagerController shelterManagerController;

    private AnimalShelter shelter1;
    private AnimalShelter shelter2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Przygotowanie przykładowych danych
        shelter1 = new AnimalShelter();
        shelter1.setId(1L);
        shelter1.setShelterName("Shelter 1");
        shelter1.setMaxCapacity(50);

        shelter2 = new AnimalShelter();
        shelter2.setId(2L);
        shelter2.setShelterName("Shelter 2");
        shelter2.setMaxCapacity(30);
    }

    // Test: Pobiera wszystkie schroniska
    @Test
    void testGetAllShelters_Success() {
        List<AnimalShelter> shelters = Arrays.asList(shelter1, shelter2);
        when(shelterManagerService.getAllShelters()).thenReturn(shelters);

        ResponseEntity<List<AnimalShelter>> response = shelterManagerController.getAllShelters();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Shelter 1", response.getBody().get(0).getShelterName());
    }

    @Test
    void testGetAllShelters_NoContent() {
        when(shelterManagerService.getAllShelters()).thenReturn(Arrays.asList());

        ResponseEntity<List<AnimalShelter>> response = shelterManagerController.getAllShelters();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetAllShelters_Exception() {
        when(shelterManagerService.getAllShelters()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<AnimalShelter>> response = shelterManagerController.getAllShelters();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Test: Pobiera podsumowanie schronisk
    @Test
    void testGetShelterSummary_Success() {
        String summary = "Total Shelters: 2";
        when(shelterManagerService.getShelterSummary()).thenReturn(summary);

        ResponseEntity<String> response = shelterManagerController.getShelterSummary();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(summary, response.getBody());
    }

    @Test
    void testGetShelterSummary_Exception() {
        when(shelterManagerService.getShelterSummary()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<String> response = shelterManagerController.getShelterSummary();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}