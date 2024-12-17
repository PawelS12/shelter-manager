package com.example.springboot.controller;

import com.example.springboot.model.Animal;
import com.example.springboot.service.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class AnimalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnimalService animalService;

    @InjectMocks
    private AnimalController animalController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(animalController).build();
    }

    @Test
    public void testAddAnimal() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Lion");
        animal.setAge(3);

        when(animalService.save(any(Animal.class))).thenReturn(animal);

        mockMvc.perform(post("/api/animal")
                        .contentType("application/json")
                        .content("{\"name\":\"Lion\",\"age\":3}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lion"))
                .andExpect(jsonPath("$.age").value(3));
    }

    @Test
    public void testDeleteAnimal() throws Exception {
        Long animalId = 1L;
        when(animalService.deleteById(animalId)).thenReturn(true);

        mockMvc.perform(delete("/api/animal/{id}", animalId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteAnimalNotFound() throws Exception {
        Long animalId = 1L;
        when(animalService.deleteById(animalId)).thenReturn(false);

        mockMvc.perform(delete("/api/animal/{id}", animalId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAnimal() throws Exception {
        Long animalId = 1L;
        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setName("Lion");
        animal.setAge(3);

        when(animalService.findById(animalId)).thenReturn(animal);

        mockMvc.perform(get("/api/animal/{id}", animalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(animalId))
                .andExpect(jsonPath("$.name").value("Lion"))
                .andExpect(jsonPath("$.age").value(3));
    }

    @Test
    public void testGetAnimalNotFound() throws Exception {
        Long animalId = 1L;
        when(animalService.findById(animalId)).thenReturn(null);

        mockMvc.perform(get("/api/animal/{id}", animalId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllAnimals() throws Exception {
        Animal animal1 = new Animal();
        animal1.setId(1L);
        animal1.setName("Lion");
        animal1.setAge(3);

        Animal animal2 = new Animal();
        animal2.setId(2L);
        animal2.setName("Elephant");
        animal2.setAge(10);

        when(animalService.findAll()).thenReturn(Arrays.asList(animal1, animal2));

        mockMvc.perform(get("/api/animal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Lion"))
                .andExpect(jsonPath("$[1].name").value("Elephant"));
    }

    @Test
    public void testGetAllAnimalsNoContent() throws Exception {
        when(animalService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/animal"))
                .andExpect(status().isNoContent());
    }
}