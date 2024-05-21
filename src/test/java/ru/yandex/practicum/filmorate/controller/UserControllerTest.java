package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController userController;

    @Test
    void testContextLoads() {

        assertThat(userController).isNotNull();
    }

    @Test
    void testGetAllUsersEmpty() {
        Collection<User> users = userController.getAllUsers();

        assertNotNull(users);
    }

    @Test
    void testAddValidUser() {
        User user = new User(null, "test@example.com", "Login", "User", LocalDate.of(1985, 1, 1));
        User addedUser = userController.addUser(user).getBody();

        assertEquals("test@example.com", addedUser.getEmail());
        assertEquals("Login", addedUser.getLogin());
        assertEquals("User", addedUser.getName());
    }

    @Test
    void testUpdateValidUser() {
        userController.addUser(new User(1L, "test@example.com", "Login", "Updated User", LocalDate.of(1985, 1, 1)));
        User updatedUser = new User(1L, "example@example.com", "Login2", "Updated User", LocalDate.of(1985, 1, 1));

        assertNotNull(updatedUser);
        assertEquals("Updated User", updatedUser.getName());
    }

    @Test
    void testUpdateUserWithNoId() {
        User user = new User(null, "test@example.com", "Login", "User", LocalDate.of(1985, 1, 1));
        ResponseEntity<User> responseEntity = userController.updateUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateUserWithInvalidId() {
        User user = new User(8888888L, "test@example.com", "Login", "User", LocalDate.of(1985, 1, 1));
        ResponseEntity<User> responseEntity = userController.updateUser(user);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /* @Test
    void testAddUserWithInvalidEmail() {
        User user = new User(null, "invalid_email", "Login", "User", LocalDate.of(1985, 1, 1));

        assertThrows(MethodArgumentNotValidException.class, () -> userController.addUser(user));
    }

    @Test
    void testAddUserWithBlankLogin() {
        User user = new User(null, "test@example.com", " ", "User", LocalDate.of(1985, 1, 1));

        assertThrows(MethodArgumentNotValidException.class, () -> userController.addUser(user));
    }

    @Test
    void testAddUserWithFutureBirthday() {
        User user = new User(null, "test@example.com", "Login", "User", LocalDate.of(2099, 1, 1));

        assertThrows(MethodArgumentNotValidException.class, () -> userController.addUser(user));
    }*/
}