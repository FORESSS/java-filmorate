package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private User user;

    @BeforeEach
    public void create() {
        user = new User(1L, "test@example.com", "User", "User", LocalDate.of(1985, 1, 1));
    }

    @Test
    public void testGetAllUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetUserById() {
        restTemplate.postForEntity("/users", user, User.class);
        ResponseEntity<User> response = restTemplate.getForEntity("/users/1", User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreateUser() {
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateUser() {
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

    // Добавленные методы тестирования
    @Test
    public void testAddFriend() {
        restTemplate.postForEntity("/users", user, User.class);
        restTemplate.postForEntity("/users", new User(2L, "friend@example.com", "Friend", "Friend",
                LocalDate.of(1987, 1, 1)), User.class);
        restTemplate.put("/users/1/friends/2", null);
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users/1/friends", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRemoveFriend() {
        restTemplate.postForEntity("/users", user, User.class);
        restTemplate.postForEntity("/users", new User(2L, "friend@example.com", "Friend", "Friend",
                LocalDate.of(1987, 1, 1)), User.class);
        restTemplate.put("/users/1/friends/2", null);
        restTemplate.delete("/users/1/friends/2");
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users/1/friends", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetFriends() {
        restTemplate.postForEntity("/users", user, User.class);
        restTemplate.postForEntity("/users", new User(2L, "friend@example.com", "Friend", "Friend",
                LocalDate.of(1987, 1, 1)), User.class);
        restTemplate.put("/users/1/friends/2", null);
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users/1/friends", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetCommonFriends() {
        restTemplate.postForEntity("/users", user, User.class);
        restTemplate.postForEntity("/users", new User(2L, "friend@example.com", "Friend", "Friend",
                LocalDate.of(1985, 1, 1)), User.class);
        restTemplate.postForEntity("/users", new User(3L, "common@example.com", "Common User", "Common User",
                LocalDate.of(1987, 1, 1)), User.class);
        restTemplate.put("/users/1/friends/3", null);
        restTemplate.put("/users/2/friends/3", null);
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users/1/friends/common/2", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateUserWithInvalidId() {
        user.setId(100L);
        ResponseEntity<Void> response = restTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(user), Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddUserWithInvalidEmail() {
        user.setEmail("invalid-email");
        ResponseEntity<Void> response = restTemplate.postForEntity("/users", user, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAddUserWithBlankLogin() {
        user.setLogin("");
        ResponseEntity<Void> response = restTemplate.postForEntity("/users", user, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAddUserWithFutureBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        ResponseEntity<Void> response = restTemplate.postForEntity("/users", user, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddFriendWithNonExistingUser() {
        ResponseEntity<Void> response = restTemplate.exchange("/users/100/friends/2", HttpMethod.PUT, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddFriendWithNonExistingFriend() {
        ResponseEntity<Void> response = restTemplate.exchange("/users/1/friends/200", HttpMethod.PUT, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddFriendWithSameUser() {
        ResponseEntity<Void> response = restTemplate.exchange("/users/1/friends/1", HttpMethod.PUT, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddFriendThatIsAlreadyAFriend() {
        restTemplate.exchange("/users/111/friends/222", HttpMethod.PUT, null, Void.class);
        ResponseEntity<Void> response = restTemplate.exchange("/users/111/friends/222", HttpMethod.PUT, null, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}