package com.example.springboot.controller;

import com.example.springboot.model.Animal;
import com.example.springboot.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    // 1. POST /api/animal - Dodaje nowe zwierzę do schroniska
    @PostMapping
    public ResponseEntity<Animal> addAnimal(@RequestBody Animal animal) {
        try {
            Animal savedAnimal = animalService.save(animal);
            return new ResponseEntity<>(savedAnimal, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 2. DELETE /api/animal/{id} - Usuwa zwierzę ze schroniska
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        try {
            if (animalService.deleteById(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 3. GET /api/animal/{id} - Zwraca informacje o zwierzęciu
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimal(@PathVariable Long id) {
        try {
            Animal animal = animalService.findById(id);
            if (animal != null) {
                return ResponseEntity.ok(animal);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 4. GET /api/animal - Zwraca listę wszystkich zwierząt
    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        try {
            List<Animal> animals = animalService.findAll();

            if (animals == null || animals.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(animals);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}