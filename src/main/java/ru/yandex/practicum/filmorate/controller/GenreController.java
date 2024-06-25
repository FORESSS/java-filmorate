package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.genre.GenreDTO;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<GenreDTO> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDTO getGenreById(@PathVariable("genreId") int genreId) {
        return genreService.getGenreById(genreId);
    }
}