package ru.yandex.practicum.filmorate.request;

public class UserTextRequests {
    public static final String FIND_ALL = "SELECT * FROM users";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    public static final String INSERT =
            "INSERT INTO users(email, login, username, birthday) VALUES (?, ?, ?, ?)";
    public static final String UPDATE =
            "UPDATE users SET email = ?, login = ?, username = ?, birthday = ? WHERE id = ?";
    public static final String FIND_BY_ID = "SELECT * FROM users WHERE id=?";
    public static final String FIND_BY_PARAM = "SELECT * FROM users WHERE email = ? AND login=? AND birthday=?";
    public static final String GET_ALL_FRIENDS =
            "SELECT id, email,login, username, birthday FROM users JOIN friends ON id=friend_id WHERE user_id=?";
    public static final String GET_MUTUAL_FRIENDS =
            "SELECT id, email,login, username, birthday\n" +
                    "FROM users JOIN friends ON id=friend_id\n" +
                    "WHERE user_id=?\n" +
                    "INTERSECT\n" +
                    "SELECT id, email,login, username, birthday\n" +
                    "FROM users JOIN friends ON id=friend_id\n" +
                    "WHERE user_id=?";
}