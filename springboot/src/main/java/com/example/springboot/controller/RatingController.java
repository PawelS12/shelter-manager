package com.example.springboot.controller;

import com.example.springboot.model.Rating;
import com.example.springboot.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // 1. POST /api/rating/{shelterId} - Dodaje ocenę dla schroniska
    @PostMapping("/{shelterId}")
    public ResponseEntity<Rating> addRating(@PathVariable Long shelterId, @RequestBody Rating rating) {
        try {
            Rating savedRating = ratingService.addRating(shelterId, rating);
            return new ResponseEntity<>(savedRating, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 2. GET /api/rating/{shelterId} - Pobiera wszystkie oceny dla schroniska
    @GetMapping("/{shelterId}")
    public ResponseEntity<List<Rating>> getRatingsByShelterId(@PathVariable Long shelterId) {
        try {
            List<Rating> ratings = ratingService.getRatingsByShelterId(shelterId);
            if (ratings != null && !ratings.isEmpty()) {
                return new ResponseEntity<>(ratings, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. GET /api/rating/{shelterId}/average - Pobiera średnią ocenę dla schroniska
    @GetMapping("/{shelterId}/average")
    public ResponseEntity<String> getAverageRating(@PathVariable Long shelterId) {
        try {
            String averageRating = ratingService.getAverageRating(shelterId);
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}