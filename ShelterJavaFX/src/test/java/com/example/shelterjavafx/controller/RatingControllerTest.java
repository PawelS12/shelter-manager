package com.example.shelterjavafx.controller;

import com.example.shelterjavafx.model.Rating;
import com.example.shelterjavafx.repository.RatingRepository;
import com.example.shelterjavafx.repository.AnimalShelterRepository;
import com.example.shelterjavafx.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = RatingController.class)
class RatingControllerTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private AnimalShelterRepository shelterRepository;

    @InjectMocks
    private RatingService ratingService;

    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Przygotowanie przykładowych danych
        rating = new Rating();
        rating.setId(1L);
        rating.setValue(5);
        rating.setComment("Excellent!");
    }

    // Test: Zapisuje ocenę w bazie
    @Test
    void testSaveRating_Success() {
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating savedRating = ratingService.save(rating);

        assertNotNull(savedRating);
        assertEquals(5, savedRating.getValue());
        assertEquals("Excellent!", savedRating.getComment());
    }

    @Test
    void testSaveRating_Exception() {
        when(ratingRepository.save(any(Rating.class))).thenThrow(new RuntimeException("Error"));

        Rating savedRating = ratingService.save(rating);

        assertNull(savedRating);
    }

    // Test: Pobiera wszystkie oceny dla danego schroniska
    @Test
    void testGetRatingsByShelterId_Success() {
        List<Rating> ratings = Arrays.asList(rating);
        when(ratingRepository.findByShelterId(1L)).thenReturn(ratings);

        List<Rating> result = ratingService.getRatingsByShelterId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getValue());
        assertEquals("Excellent!", result.get(0).getComment());
    }

    @Test
    void testGetRatingsByShelterId_NoRatings() {
        when(ratingRepository.findByShelterId(1L)).thenReturn(Arrays.asList());

        List<Rating> result = ratingService.getRatingsByShelterId(1L);

        assertTrue(result.isEmpty());
    }

    // Test: Pobiera średnią ocenę dla schroniska
    @Test
    void testGetAverageRating_Success() {
        List<Rating> ratings = Arrays.asList(
                new Rating(5, null, null, "Excellent!"),
                new Rating(4, null, null, "Good!")
        );
        when(ratingRepository.findByShelterId(1L)).thenReturn(ratings);

        String average = ratingService.getAverageRating(1L);

        assertNotNull(average);
        assertEquals("4.5 (2)", average);
    }

    @Test
    void testGetAverageRating_NoRatings() {
        when(ratingRepository.findByShelterId(1L)).thenReturn(Arrays.asList());

        String average = ratingService.getAverageRating(1L);

        assertNotNull(average);
        assertEquals("No ratings (0)", average);
    }
}