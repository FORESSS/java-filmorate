package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequestDTO;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequestDTO;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.friends.FriendsRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.utils.UserMapper;

import java.util.Collection;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    public Collection<UserDTO> getAllUsers() {
        log.info("Возврат списка пользователей");
        return userRepository
                .getAllUsers()
                .stream()
                .map(UserMapper::userToDTO)
                .toList();
    }

    public UserDTO addUser(NewUserRequestDTO dto) {
        User user = UserMapper.newUserRequestDTOToUser(dto);
        setName(user);
        User newUser = userRepository.addUser(user);
        log.info("Добавлен пользователь с id: {}", newUser.getId());
        return UserMapper.userToDTO(newUser);
    }

    public UserDTO updateUser(UpdateUserRequestDTO dto) {
        User user = UserMapper.updateUserRequestDTOToUser(dto);
        checkUserId(user);
        setName(user);
        userRepository.updateUser(user);
        log.info("Обновлены данные пользователя с id: {}", user.getId());
        return UserMapper.userToDTO(user);
    }

    public void sendRequestForFriendship(Integer applicantId, Integer approvingId) {
        checkUserId(applicantId);
        checkUserId(approvingId);
        checkSameUsers(applicantId, approvingId);
        friendsRepository.sendRequestForFriendship(applicantId, approvingId);
        log.info("Пользователь с id: {} отправил запрос на дружбу пользователю с id: {}", applicantId, approvingId);
    }

    public void recallRequestForFriendship(Integer applicantId, Integer approvingId) {
        checkUserId(applicantId);
        checkUserId(approvingId);
        checkSameUsers(applicantId, approvingId);
        friendsRepository.recallRequestForFriendship(applicantId, approvingId);
        log.info("Пользователь с id: {} отозвал запрос на дружбу с пользователем с id: {}", applicantId, approvingId);
    }

    public Collection<UserDTO> getAllFriends(Integer userId) {
        checkUserId(userId);
        log.info("Возврат списка друзей пользователя с id: {}", userId);
        return userRepository
                .getAllFriends(userId)
                .stream()
                .map(UserMapper::userToDTO)
                .toList();
    }

    public Collection<UserDTO> getMutualFriends(Integer userId, Integer otherId) {
        checkUserId(userId);
        checkUserId(otherId);
        checkSameUsers(userId, otherId);
        log.info("Возврат списка общих друзей пользователей с id: {} и {}", userId, otherId);
        return userRepository
                .getMutualFriends(userId, otherId)
                .stream()
                .map(UserMapper::userToDTO)
                .toList();
    }

    private void checkUserId(User user) {
        Integer userId = user.getId();
        if (isNull(userId)) {
            throw new IdNotFoundException("ID не указан");
        } else if (!userRepository.containsUserById(userId)) {
            throw new IdNotFoundException("Пользователь с id: " + userId + " не найден");
        }
    }

    private void checkUserId(int userId) {
        if (!userRepository.containsUserById(userId)) {
            throw new IdNotFoundException("Пользователь с id: " + userId + " не найден");
        }
    }

    private void checkSameUsers(int offerId, int acceptId) {
        if (offerId == acceptId) {
            throw new ObjectAlreadyExistsException("Нельзя добавить пользователя в друзья", offerId);
        }
    }

    private void setName(User user) {
        String name = user.getName();
        if (isNull(name) || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }
}