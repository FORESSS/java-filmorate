package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.rating.RatingDTO;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<RatingDTO> getAllMPARatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.OK)
    public RatingDTO getGenreById(@PathVariable("ratingId") int ratingId) {
        return ratingService.getRatingById(ratingId);
    }
}