package ru.yandex.practicum.filmorate.controller;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void create() {
        userController = new UserController();
    }
    @Test
    void testGetAllUsersEmpty() {
        Collection<User> users = userController.getAllUsers();
        assertEquals(0, users.size());
    }
    @Test
    void testAddValidUser() {
        User user = new User(null, "test@example.com", "test_login", "User", LocalDate.of(1985, 1, 1));
        User addedUser = userController.addUser(user);
        assertEquals("test@example.com", addedUser.getEmail());
        assertEquals("test_login", addedUser.getLogin());
        assertEquals("User", addedUser.getName());
    }
    @Test
    void testAddUserWithInvalidEmail() {
        User user = new User(null, "invalid_email", "test_login", "User", LocalDate.of(1985, 1, 1));
        assertThrows(MethodArgumentNotValidException.class, () -> userController.addUser(user));
    }

    @Test
    void testAddUserWithBlankLogin() {
        User user = new User(null, "test@example.com", " ", "User", LocalDate.of(1985, 1, 1));
        assertThrows(ConstraintViolationException.class, () -> userController.addUser(user));
    }

    @Test
    void testAddUserWithFutureBirthday() {
        User user = new User(null, "test@example.com", "test_login", "Test User", LocalDate.of(2099, 1, 1));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void testUpdateValidUser() {
        User user = new User(1L, "test@example.com", "test_login", "Updated User", LocalDate.of(1990, 1, 1));
        User updatedUser = userController.updateUser(user);
        assertEquals(1, updatedUser.getId());
        assertEquals("Updated User", updatedUser.getName());
    }

    @Test
    void testUpdateUserWithNoId() {
        User user = new User(null, "test@example.com", "test_login", "Test User", LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }

    @Test
    void testUpdateUserWithInvalidId() {
        User user = new User(100L, "test@example.com", "test_login", "Test User", LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }

 /*   @Test
    void checkName_NullName() {
        User user = new User(null, "test@example.com", "test_login", null, LocalDate.of(1990, 1, 1));
        userController.checkName(user);
        assertEquals("test_login", user.getName());
    }

    @Test
    void checkName_BlankName() {
        User user = new User(null, "test@example.com", "test_login", " ", LocalDate.of(1990, 1, 1));
        userController.checkName(user);
        assertEquals("test_login", user.getName());
    }

    @Test
    void checkName_ValidName() {
        User user = new User(null, "test@example.com", "test_login", "Test User", LocalDate.of(1990, 1, 1));
        userController.checkName(user);
        assertEquals("Test User", user.getName());
    }*/
}