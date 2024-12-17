package com.example.springboot.controller;

import com.example.springboot.model.AnimalShelter;
import com.example.springboot.service.ShelterManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sheltermanager")
public class ShelterManagerController {

    private final ShelterManagerService shelterManagerService;

    public ShelterManagerController(ShelterManagerService shelterManagerService) {
        this.shelterManagerService = shelterManagerService;
    }

    // 1. GET /api/sheltermanager - Pobiera wszystkie schroniska
    @GetMapping
    public ResponseEntity<List<AnimalShelter>> getAllShelters() {
        try {
            List<AnimalShelter> shelters = shelterManagerService.getAllShelters();
            if (shelters != null && !shelters.isEmpty()) {
                return new ResponseEntity<>(shelters, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. GET /api/sheltermanager/summary - Pobiera podsumowanie schronisk
    @GetMapping("/summary")
    public ResponseEntity<String> getShelterSummary() {
        try {
            String summary = shelterManagerService.getShelterSummary();
            return new ResponseEntity<>(summary, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}