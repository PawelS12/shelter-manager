package com.example.springboot.controller;

import com.example.springboot.model.AnimalShelter;
import com.example.springboot.service.ShelterManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShelterManagerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShelterManagerService shelterManagerService;

    @InjectMocks
    private ShelterManagerController shelterManagerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shelterManagerController).build();
    }

    // Test for GET /api/sheltermanager
    @Test
    void testGetAllShelters() throws Exception {
        AnimalShelter shelter1 = new AnimalShelter();
        shelter1.setShelterName("Shelter 1");

        AnimalShelter shelter2 = new AnimalShelter();
        shelter2.setShelterName("Shelter 2");

        // Mock the service to return a list of shelters
        when(shelterManagerService.getAllShelters()).thenReturn(Arrays.asList(shelter1, shelter2));

        mockMvc.perform(get("/api/sheltermanager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].shelterName").value("Shelter 1"))
                .andExpect(jsonPath("$[1].shelterName").value("Shelter 2"));
    }

    // Test for GET /api/sheltermanager when no shelters are available
    @Test
    void testGetAllSheltersNoContent() throws Exception {
        // Mock the service to return an empty list
        when(shelterManagerService.getAllShelters()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/sheltermanager"))
                .andExpect(status().isNoContent());
    }

    // Test for GET /api/sheltermanager/summary
    @Test
    void testGetShelterSummary() throws Exception {
        // Mock the service to return a summary
        String summary = "There are 5 shelters in the system.";
        when(shelterManagerService.getShelterSummary()).thenReturn(summary);

        mockMvc.perform(get("/api/sheltermanager/summary"))
                .andExpect(status().isOk())
                .andExpect(content().string(summary));
    }

    // Test for GET /api/sheltermanager/summary when an error occurs
    @Test
    void testGetShelterSummaryInternalServerError() throws Exception {
        // Mock the service to throw an exception
        when(shelterManagerService.getShelterSummary()).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/sheltermanager/summary"))
                .andExpect(status().isInternalServerError());
    }
}
