package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.rating.RatingDTO;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.repository.mparating.RatingRepository;
import ru.yandex.practicum.filmorate.utils.RatingMapper;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public Collection<RatingDTO> getAllRatings() {
        log.info("Возврат списка рейтинга");
        return ratingRepository
                .getAllRatings()
                .stream()
                .map(RatingMapper::ratingToDTO)
                .toList();
    }

    public RatingDTO getRatingById(int ratingId) {
        log.info("Возврат рейтинга с id: {}", ratingId);
        return ratingRepository
                .getRatingById(ratingId)
                .map(RatingMapper::ratingToDTO)
                .orElseThrow(() -> new IdNotFoundException("Рейтинг с id: " + ratingId + " не найден"));
    }
}