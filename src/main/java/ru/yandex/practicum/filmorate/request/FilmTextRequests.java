package ru.yandex.practicum.filmorate.request;

public class FilmTextRequests {
    public static final String GET_ALL_FILMS = "SELECT * FROM films";
    public static final String GET_FILM_BY_ID = "SELECT * FROM films WHERE id=?";
    public static final String INSERT =
            "INSERT INTO films(name, description, release_date, duration, mpa_rating_id) VALUES (?,?,?,?,?)";
    public static final String INSERT_GENRES = "INSERT INTO film_genres(film_id,genre_id) VALUES (?,?)";
    public static final String UPDATE =
            "UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_rating_id=? WHERE id = ?";
    public static final String DELETE_GENRES = "DELETE FROM film_genres WHERE film_id=?";
    public static final String ADD_USERLIKE_TO_FILM = "INSERT INTO film_likes(film_id,user_id) VALUES (?,?);";
    public static final String DELETE_USERLIKE_TO_FILM = "DELETE FROM film_likes WHERE film_id=? AND user_id=?";
    public static final String CONTAINS_FILM_BY_VALUE =
            "SELECT EXISTS (SELECT 1 FROM films WHERE name=? AND release_date=? AND duration=?)";
    public static final String GET_USERLIKE_TO_FILM = "SELECT COUNT(*) FROM film_likes WHERE film_id=? AND user_id=?";
    public static final String GET_FILM_LIKES = "SELECT COUNT(*) FROM film_likes WHERE film_id=?";
    public static final String GET_MOST_POPULAR_FILMS =
            "SELECT f.id,f.name,f.description, f.release_date, f.duration, f.mpa_rating_id FROM films AS f LEFT JOIN" +
                    "(SELECT film_id, COUNT(*) AS count FROM film_likes GROUP BY film_id) AS t ON f.id=t.film_id " +
                    "ORDER BY count DESC LIMIT ?;";
//    public static final String GET_ALL_FILMS_WITH_GENRES =
//            "SELECT f.*, fg.genre_id " +
//                    "FROM films AS f " +
//                    "LEFT JOIN film_genres AS fg ON f.id = fg.film_id";
//
//    public static final String GET_MOST_POPULAR_FILMS_WITH_GENRES =
//            "SELECT f.*, fg.genre_id " +
//                    "FROM films AS f " +
//                    "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
//                    "ORDER BY (SELECT COUNT(*) FROM film_likes WHERE film_id = f.id) DESC " +
//                    "LIMIT ?";
}