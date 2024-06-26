package ru.yandex.practicum.filmorate.repository.friends;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.request.FriendsTextRequests;

import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static ru.yandex.practicum.filmorate.model.FriendshipStatus.*;

@Repository
@Primary
public class FriendsDBRepository extends BaseRepository<Friends> implements FriendsRepository {
    public FriendsDBRepository(JdbcTemplate jdbc, RowMapper<Friends> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public FriendshipStatus getFriendshipStatus(Integer applicantId, Integer approvingId) {
        Optional<Friends> result = findOne(FriendsTextRequests.GET_FRIENDSHIP_STATUS, applicantId, approvingId);
        if (result.isEmpty()) {
            return ABSENT;
        }
        if (result.get().getStatus().equals(UNKNOWN)) {
            throw new InternalServerException("Некорректный статус");
        }
        return result.get().getStatus();
    }

    @Override
    public void setFriendshipStatus(Integer applicantId, Integer approvingId, FriendshipStatus status) {
        if (status.equals(UNKNOWN) || status.equals(ABSENT)) {
            throw new InternalServerException("Некорректный статус");
        }
        update(
                FriendsTextRequests.SET_FRIENDSHIP_STATUS,
                status.getDatabaseId(),
                applicantId,
                approvingId
        );
    }

    @Override
    public void addFriendshipStatus(Integer applicantId, Integer approvingId, FriendshipStatus status) {
        insertMultKeys(
                FriendsTextRequests.ADD_FRIENDSHIP_STATUS,
                applicantId,
                approvingId,
                status.getDatabaseId()
        );
    }

    @Override
    public void deleteFriendshipStatus(Integer applicantId, Integer approvingId) {
        delete(FriendsTextRequests.DELETE_FRIENDSHIP, applicantId, approvingId);
    }

    @Transactional(isolation = REPEATABLE_READ)
    @Override
    public void sendRequestForFriendship(Integer applicantId, Integer approvingId) {
        var applicantToApprovingStatus = getFriendshipStatus(applicantId, approvingId);
        var approvingToApplicantStatus = getFriendshipStatus(approvingId, applicantId);

        if (applicantToApprovingStatus.equals(ABSENT) && approvingToApplicantStatus.equals(ABSENT)) {
            addFriendshipStatus(applicantId, approvingId, REQUESTED);
            return;
        }
        if (applicantToApprovingStatus.equals(ABSENT) && approvingToApplicantStatus.equals(REQUESTED)) {
            addFriendshipStatus(applicantId, approvingId, CONFIRMED);
            setFriendshipStatus(approvingId, applicantId, CONFIRMED);
            return;
        }
        throw new ObjectAlreadyExistsException("Запрос уже отправлен", approvingId);
    }

    @Transactional(isolation = REPEATABLE_READ)
    @Override
    public void recallRequestForFriendship(Integer applicantId, Integer approvingId) {
        var applicantToApprovingStatus = getFriendshipStatus(applicantId, approvingId);
        var approvingToApplicantStatus = getFriendshipStatus(approvingId, applicantId);
        if (applicantToApprovingStatus.equals(ABSENT)) {
            return;
        }
        deleteFriendshipStatus(applicantId, approvingId);
        if (approvingToApplicantStatus.equals(CONFIRMED)) {
            setFriendshipStatus(approvingId, applicantId, REQUESTED);
        }
    }
}