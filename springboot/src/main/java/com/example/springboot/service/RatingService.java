package com.example.springboot.service;

import com.example.springboot.model.AnimalShelter;
import com.example.springboot.model.Rating;
import com.example.springboot.repository.AnimalShelterRepository;
import com.example.springboot.repository.RatingRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final AnimalShelterRepository shelterRepository;

    public RatingService(RatingRepository ratingRepository, AnimalShelterRepository shelterRepository) {
        this.ratingRepository = ratingRepository;
        this.shelterRepository = shelterRepository;
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsByShelterId(Long shelterId) {
        return ratingRepository.findByShelterId(shelterId);
    }

    public String getAverageRating(Long shelterId) {
        List<Rating> ratings = getRatingsByShelterId(shelterId);
        if (ratings == null || ratings.isEmpty()) {
            return "No ratings (0)";
        }

        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getValue();
        }

        double average = sum / ratings.size();
        return String.format("%.1f (%d)", average, ratings.size());
    }

    public Rating addRating(Long shelterId, Rating rating) {
        AnimalShelter shelter = shelterRepository.findById(shelterId).orElseThrow(() -> new IllegalArgumentException("Shelter with ID " + shelterId + " does not exist."));
        rating.setShelter(shelter);
        return ratingRepository.save(rating);
    }

}