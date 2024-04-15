package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

}

/*    User save(User user);

    User update(Map<String, String> userUpdate, Long userId);

    User findUser(Long userId);

    void deleteUser(Long userId);

    Collection<User> findAll();

    void checkUserId(Long userId);*/