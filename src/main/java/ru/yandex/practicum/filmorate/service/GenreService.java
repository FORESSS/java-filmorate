package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreDTO;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.utils.GenreMapper;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Collection<GenreDTO> getAllGenres() {
        log.info("Возврат списка жанров");
        return genreRepository
                .getAllGenres()
                .stream()
                .map(GenreMapper::genreToDTO)
                .toList();
    }

    public GenreDTO getGenreById(int genreId) {
        log.info("Возврат жанра с id: {}", genreId);
        return genreRepository
                .getGenreById(genreId)
                .map(GenreMapper::genreToDTO)
                .orElseThrow(() -> new IdNotFoundException("Жанр с id: " + genreId + " не найден"));
    }
}