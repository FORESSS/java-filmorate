package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequestDTO;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDTO;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("GET /users")
    void getAllUsersTest() throws Exception {
        Collection<UserDTO> userDTOList = Collections.singletonList(createUserDTO());
        Mockito.when(userService.getAllUsers()).thenReturn(userDTOList);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
        Mockito.verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("GET /users/{id}")
    void getUserByIdTest() throws Exception {
        int userId = 1;
        UserDTO userDTO = createUserDTO();
        Mockito.when(userService.getUserById(userId)).thenReturn(userDTO);
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk());
        Mockito.verify(userService).getUserById(userId);
    }

    @Test
    @DisplayName("POST /users")
    void addUserTest() throws Exception {
        NewUserRequestDTO dto = NewUserRequestDTO.builder()
                .email("test@test.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1985, 1, 1))
                .build();
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        Mockito.verify(userService).addUser(any(NewUserRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /users")
    void updateUserTest() throws Exception {
        UpdateUserRequestDTO dto = UpdateUserRequestDTO.builder()
                .id(2)
                .email("test@test.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1985, 1, 1))
                .build();
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        Mockito.verify(userService).updateUser(any(UpdateUserRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /users/{id}/friends/{friendId}")
    void sendRequestForFriendshipTest() throws Exception {
        int userId = 1;
        int friendId = 2;
        mockMvc.perform(put("/users/{id}/friends/{friendId}", userId, friendId))
                .andExpect(status().isOk());
        Mockito.verify(userService).sendRequestForFriendship(eq(userId), eq(friendId));
    }

    @Test
    @DisplayName("DELETE /users/{id}/friends/{friendId}")
    void recallRequestForFriendshipTest() throws Exception {
        int userId = 1;
        int friendId = 2;
        mockMvc.perform(delete("/users/{id}/friends/{friendId}", userId, friendId))
                .andExpect(status().isOk());
        Mockito.verify(userService).recallRequestForFriendship(eq(userId), eq(friendId));
    }

    @Test
    @DisplayName("GET /users/{id}/friends")
    void getAllFriendsTest() throws Exception {
        int userId = 1;
        Collection<UserDTO> userDTOList = Collections.singletonList(createUserDTO());
        Mockito.when(userService.getAllFriends(userId)).thenReturn(userDTOList);
        mockMvc.perform(get("/users/{id}/friends", userId))
                .andExpect(status().isOk());
        Mockito.verify(userService).getAllFriends(userId);
    }

    @Test
    @DisplayName("GET /users/{id}/friends/common/{otherId}")
    void getMutualFriendsTest() throws Exception {
        int userId = 1;
        int otherId = 2;
        Collection<UserDTO> userDTOList = Collections.singletonList(createUserDTO());
        Mockito.when(userService.getMutualFriends(userId, otherId)).thenReturn(userDTOList);
        mockMvc.perform(get("/users/{id}/friends/common/{otherId}", userId, otherId))
                .andExpect(status().isOk());
        Mockito.verify(userService).getMutualFriends(userId, otherId);
    }

    private UserDTO createUserDTO() {
        return UserDTO.builder()
                .id(1)
                .email("test@test.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1985, 1, 1))
                .build();
    }
}