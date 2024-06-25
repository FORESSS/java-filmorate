package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FriendshipStatus {
    REQUESTED(1),
    CONFIRMED(2),
    ABSENT(-1),
    UNKNOWN(-2);
    private final int databaseId;
}