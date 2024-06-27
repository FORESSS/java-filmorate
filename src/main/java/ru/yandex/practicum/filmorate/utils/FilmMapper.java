package ru.yandex.practicum.filmorate.utils;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequestDTO;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDTO;
import ru.yandex.practicum.filmorate.dto.genre.GenreDTO;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequestDTO;
import ru.yandex.practicum.filmorate.dto.rating.RatingDTO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.rating.RatingRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FilmMapper {
    private final Map<Integer, Genre> allGenres = new HashMap<>();
    private final Map<Integer, Rating> allRatings = new HashMap<>();

    public FilmMapper(GenreRepository genreRepository, RatingRepository ratingRepository) {
        genreRepository.getAllGenres().forEach(genre -> allGenres.put(genre.getId(), genre));
        ratingRepository.getAllRatings().forEach(mpaRating -> allRatings.put(mpaRating.getId(), mpaRating));
    }

    public FilmDTO filmToDTO(Film film) {
        List<GenreDTO> genreDTOS = film
                .getGenresId()
                .stream()
                .map(allGenres::get)
                .map(GenreMapper::genreToDTO)
                .toList();
        Rating mpaRating = allRatings.get(film.getMpaId());
        RatingDTO ratingDTO = RatingMapper.ratingToDTO(mpaRating);
        return FilmDTO.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(ratingDTO)
                .genres(genreDTOS)
                .build();
    }

    public static Film newFilmRequestToFilm(NewFilmRequestDTO dto) {
        List<GenreRequestDTO> genresDTO = dto.getGenres();
        if (genresDTO == null) {
            genresDTO = Collections.emptyList();
        }
        Set<Integer> genresId = genresDTO
                .stream()
                .map(GenreRequestDTO::getId)
                .collect(Collectors.toSet());
        return Film.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpaId(dto.getMpa().getId())
                .genresId(genresId)
                .build();
    }

    public static Film updateFilmRequestDTOToFilm(UpdateFilmRequestDTO dto) {
        List<GenreRequestDTO> genresDTO = dto.getGenres();
        if (genresDTO == null) {
            genresDTO = Collections.emptyList();
        }
        Set<Integer> genresId = genresDTO
                .stream()
                .map(GenreRequestDTO::getId)
                .collect(Collectors.toSet());
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpaId(dto.getMpa().getId())
                .genresId(genresId)
                .build();
    }
}