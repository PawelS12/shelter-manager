package com.example.springboot.controller;

import com.example.springboot.model.AnimalShelter;
import com.example.springboot.service.AnimalShelterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AnimalShelterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnimalShelterService animalShelterService;

    @InjectMocks
    private AnimalShelterController animalShelterController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(animalShelterController).build();
    }

    // Test for POST /api/animalshelter
    @Test
    void testAddShelter() throws Exception {
        AnimalShelter shelter = new AnimalShelter();
        shelter.setShelterName("Happy Shelter");
        shelter.setMaxCapacity(100);

        Mockito.when(animalShelterService.save(any(AnimalShelter.class))).thenReturn(shelter);

        mockMvc.perform(post("/api/animalshelter")
                        .contentType("application/json")
                        .content("{\"shelterName\": \"Happy Shelter\", \"maxCapacity\": 100}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shelterName").value("Happy Shelter"))
                .andExpect(jsonPath("$.maxCapacity").value(100));
    }

    // Test for DELETE /api/animalshelter/{id}
    @Test
    void testDeleteShelter() throws Exception {
        Mockito.when(animalShelterService.delete(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/animalshelter/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteShelterNotFound() throws Exception {
        Mockito.when(animalShelterService.delete(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/animalshelter/1"))
                .andExpect(status().isNotFound());
    }

    // Test for GET /api/animalshelter/{id}
    @Test
    void testGetShelterById() throws Exception {
        AnimalShelter shelter = new AnimalShelter();
        shelter.setShelterName("Happy Shelter");
        shelter.setMaxCapacity(100);

        Mockito.when(animalShelterService.findById(anyLong())).thenReturn(shelter);

        mockMvc.perform(get("/api/animalshelter/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shelterName").value("Happy Shelter"))
                .andExpect(jsonPath("$.maxCapacity").value(100));
    }

    @Test
    void testGetShelterByIdNotFound() throws Exception {
        Mockito.when(animalShelterService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/animalshelter/1"))
                .andExpect(status().isNotFound());
    }

    // Test for GET /api/animalshelter/{id}/csv
    @Test
    void testExportShelterToCSV() throws Exception {
        // Mock the AnimalShelter object
        AnimalShelter shelter = new AnimalShelter();
        shelter.setId(1L);  // Ensure that the ID matches the path variable
        shelter.setShelterName("Happy Shelter");
        shelter.setMaxCapacity(100);

        // Mock the service method to avoid real file I/O
        Mockito.doNothing().when(animalShelterService).exportShelterToCSV(anyLong(), any(String.class));

        // Perform the GET request and expect a 200 status
        mockMvc.perform(get("/api/animalshelter/1/csv"))
                .andExpect(status().isOk())  // 200 OK
                .andExpect(header().string("Content-Type", "text/csv"));  // Check Content-Type header
    }

    // Test for GET /api/animalshelter/{id}/fill
    @Test
    void testGetShelterFill() throws Exception {
        String fillStatus = "Shelter is 50/100";

        Mockito.when(animalShelterService.getShelterFill(anyLong())).thenReturn(fillStatus);

        mockMvc.perform(get("/api/animalshelter/1/fill"))
                .andExpect(status().isOk())
                .andExpect(content().string("Shelter is 50/100"));
    }
}