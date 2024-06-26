package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.dto.rating.RatingDTO;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private MockMvc mockMvc;
    @Mock
    private RatingService ratingService;
    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }

    @Test
    @DisplayName("GET /mpa")
    void getAllRatingsTest() throws Exception {
        Collection<RatingDTO> ratingDTOList = Collections.singletonList(createRatingDTO());
        Mockito.when(ratingService.getAllRatings()).thenReturn(ratingDTOList);
        mockMvc.perform(get("/mpa"))
                .andExpect(status().isOk());
        Mockito.verify(ratingService).getAllRatings();
    }

    @Test
    @DisplayName("GET /mpa/{ratingId}")
    void getRatingByIdTest() throws Exception {
        int ratingId = 1;
        RatingDTO ratingDTO = createRatingDTO();
        Mockito.when(ratingService.getRatingById(ratingId)).thenReturn(ratingDTO);
        mockMvc.perform(get("/mpa/{ratingId}", ratingId))
                .andExpect(status().isOk());
        Mockito.verify(ratingService).getRatingById(ratingId);
    }

    private RatingDTO createRatingDTO() {
        return RatingDTO.builder()
                .id(1)
                .name("PG-13")
                .build();
    }
}