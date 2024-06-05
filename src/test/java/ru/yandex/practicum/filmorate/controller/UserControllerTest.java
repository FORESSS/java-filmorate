package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "test@example.com", "testuser", "Test User", LocalDate.of(1990, 1, 1), Collections.emptySet());
    }

    @Test
    public void getAllUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getUserById() {
        restTemplate.postForEntity("/users", user, User.class);
        ResponseEntity<User> response = restTemplate.getForEntity("/users/1", User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void createUser() {
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateUser() {
        user.setName("Updated User");
        restTemplate.postForEntity("/users", user, User.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<User> response = restTemplate.exchange("/users", HttpMethod.PUT, requestEntity, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated User", response.getBody().getName());
    }

    @Test
    public void addFriend() {
        restTemplate.postForEntity("/users", user, User.class);
        restTemplate.postForEntity("/users", new User(2L, "friend@example.com", "frienduser", "Friend User", LocalDate.of(1991, 1, 1), Collections.emptySet()), User.class);
        restTemplate.put("/users/1/friends/2", null);
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users/1/friends", User[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }

    @Test
    public void removeFriend() {
        restTemplate.postForEntity("/users", user, User.class);
        restTemplate.postForEntity("/users", new User(2L, "friend@example.com", "frienduser", "Friend User", LocalDate.of(1991, 1, 1), Collections.emptySet()), User.class);
        restTemplate.put("/users/1/friends/2", null);
        restTemplate.delete("/users/1/friends/2");
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users/1/friends", User[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length);
    }

    @Test
    public void getFriends() {
        restTemplate.postForEntity("/users", user, User.class);
        restTemplate.postForEntity("/users", new User(2L, "friend@example.com", "frienduser", "Friend User", LocalDate.of(1991, 1, 1), Collections.emptySet()), User.class);
        restTemplate.put("/users/1/friends/2", null);
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users/1/friends", User[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }

    @Test
    public void getCommonFriends() {
        restTemplate.postForEntity("/users", user, User.class);
        restTemplate.postForEntity("/users", new User(2L, "friend@example.com", "frienduser", "Friend User", LocalDate.of(1991, 1, 1), Collections.emptySet()), User.class);
        restTemplate.postForEntity("/users", new User(3L, "common@example.com", "commonuser", "Common User", LocalDate.of(1992, 1, 1), Collections.emptySet()), User.class);
        restTemplate.put("/users/1/friends/3", null);
        restTemplate.put("/users/2/friends/3", null);
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users/1/friends/common/2", User[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }
}