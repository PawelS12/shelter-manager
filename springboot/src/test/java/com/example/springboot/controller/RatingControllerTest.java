package com.example.springboot.controller;

import com.example.springboot.model.Rating;
import com.example.springboot.service.RatingService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RatingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }

    // Test for POST /api/rating/{shelterId}
    @Test
    void testAddRating() throws Exception {
        Rating rating = new Rating();
        rating.setValue(5);

        // Mock the service to return the saved rating
        when(ratingService.addRating(anyLong(), any(Rating.class))).thenReturn(rating);

        mockMvc.perform(post("/api/rating/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\": 5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").value(5));
    }

    // Test for GET /api/rating/{shelterId}
    @Test
    void testGetRatingsByShelterId() throws Exception {
        Rating rating1 = new Rating();
        rating1.setValue(4);
        Rating rating2 = new Rating();
        rating2.setValue(3);

        // Mock the service to return a list of ratings
        when(ratingService.getRatingsByShelterId(anyLong())).thenReturn(Arrays.asList(rating1, rating2));

        mockMvc.perform(get("/api/rating/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].value").value(4))
                .andExpect(jsonPath("$[1].value").value(3));
    }

    // Test for GET /api/rating/{shelterId} when no ratings are available
    @Test
    void testGetRatingsByShelterIdNoContent() throws Exception {
        // Mock the service to return an empty list
        when(ratingService.getRatingsByShelterId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rating/1"))
                .andExpect(status().isNoContent());
    }

    // Test for GET /api/rating/{shelterId}/average
    @Test
    void testGetAverageRating() throws Exception {
        // Mock the service to return an average rating
        when(ratingService.getAverageRating(anyLong())).thenReturn("4.0");

        mockMvc.perform(get("/api/rating/1/average"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.0"));
    }

    // Test for GET /api/rating/{shelterId}/average when no ratings are available
    @Test
    void testGetAverageRatingNoRatings() throws Exception {
        // Mock the service to return null or some indicator when there are no ratings
        when(ratingService.getAverageRating(anyLong())).thenReturn("0.0");

        mockMvc.perform(get("/api/rating/1/average"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }
}