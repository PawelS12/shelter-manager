package com.example.springboot.controller;

import com.example.springboot.model.AnimalShelter;
import com.example.springboot.service.AnimalShelterService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/animalshelter")
public class AnimalShelterController {

    private final AnimalShelterService animalShelterService;

    public AnimalShelterController(AnimalShelterService animalShelterService) {
        this.animalShelterService = animalShelterService;
    }

    // 1. POST /api/animalshelter - Dodaje nowe schronisko
    @PostMapping
    public ResponseEntity<AnimalShelter> addShelter(@RequestBody AnimalShelter shelter) {
        try {
            AnimalShelter savedShelter = animalShelterService.save(shelter);
            return new ResponseEntity<>(savedShelter, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. DELETE /api/animalshelter/{id} - Usuwa schronisko
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) {
        try {
            boolean isDeleted = animalShelterService.delete(id);
            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. GET /api/animalshelter/{id} - Pobiera szczegóły schroniska
    @GetMapping("/{id}")
    public ResponseEntity<AnimalShelter> getShelterById(@PathVariable Long id) {
        try {
            AnimalShelter shelter = animalShelterService.findById(id);
            if (shelter != null) {
                return new ResponseEntity<>(shelter, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 4. GET /api/animalshelter/{id}/csv - Pobiera dane schroniska w formacie CSV
    @GetMapping("/{id}/csv")
    public ResponseEntity<byte[]> exportShelterToCSV(@PathVariable Long id) {
        try {
            String filename = "shelter_" + id + ".csv";
            File tempFile = File.createTempFile("shelter_" + id, ".csv");

            animalShelterService.exportShelterToCSV(id, tempFile.getAbsolutePath());

            byte[] fileContent = Files.readAllBytes(tempFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + filename);
            headers.add("Content-Type", "text/csv");

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 5. GET /api/animalshelter/{id}/fill - Sprawdza zapełnienie schroniska
    @GetMapping("/{id}/fill")
    public ResponseEntity<String> getShelterFill(@PathVariable Long id) {
        try {
            String fillStatus = animalShelterService.getShelterFill(id);
            return new ResponseEntity<>(fillStatus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}