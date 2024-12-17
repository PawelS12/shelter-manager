package com.example.shelterjavafx.controller;

import com.example.shelterjavafx.model.Animal;
import com.example.shelterjavafx.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    void testAddAnimal() throws Exception {
        Animal animal = new Animal();
        animal.setName("Dog");
        animal.setSpecies("Dog");

        mockMvc.perform(post("/api/animal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Dog\", \"type\": \"Dog\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dog"));
    }

    @Test
    void testGetAnimal() throws Exception {
        // Assuming there is an animal with ID 1
        mockMvc.perform(get("/api/animal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dog"));
    }

    @Test
    void testDeleteAnimal() throws Exception {
        // Assuming an animal exists with ID 1
        mockMvc.perform(delete("/api/animal/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllAnimals() throws Exception {
        mockMvc.perform(get("/api/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}