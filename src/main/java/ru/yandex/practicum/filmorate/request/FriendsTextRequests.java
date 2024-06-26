package ru.yandex.practicum.filmorate.request;

public class FriendsTextRequests {
    public static final String GET_FRIENDSHIP_STATUS = "SELECT * FROM friends WHERE user_id=? AND friend_id=?;";
    public static final String SET_FRIENDSHIP_STATUS =
            "UPDATE friends SET status_id=? WHERE user_id=? AND friend_id=?;";
    public static final String ADD_FRIENDSHIP_STATUS =
            "INSERT INTO friends (user_id, friend_id, status_id) VALUES (?,?,?);";
    public static final String DELETE_FRIENDSHIP = "DELETE FROM friends WHERE user_id=? AND friend_id=?;";
}