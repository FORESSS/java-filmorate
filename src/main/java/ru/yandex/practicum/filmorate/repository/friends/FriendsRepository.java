package ru.yandex.practicum.filmorate.repository.friends;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;

public interface FriendsRepository {

    FriendshipStatus getFriendshipStatus(Integer applicantId, Integer approvingId);

    void setFriendshipStatus(Integer applicantId, Integer approvingId, FriendshipStatus status);

    void addFriendshipStatus(Integer applicantId, Integer approvingId, FriendshipStatus status);

    void deleteFriendshipStatus(Integer applicantId, Integer approvingId);

    void sendRequestForFriendship(Integer applicantId, Integer approvingId);

    void recallRequestForFriendship(Integer applicantId, Integer approvingId);

}
