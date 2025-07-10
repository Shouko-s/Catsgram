package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        } else if (userExistsByEmail(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;

    }

    public User update(User newUser) {

        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException("Не найден");
        }

        User oldUser = users.get(newUser.getId());

        if (newUser.getEmail() != null) {
            if (!oldUser.getEmail().equalsIgnoreCase(newUser.getEmail()) && userExistsByEmail(newUser.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getUsername() != null) {
            oldUser.setUsername(newUser.getUsername());
        }

        if (newUser.getPassword() != null) {
            oldUser.setPassword(newUser.getPassword());
        }

        users.put(oldUser.getId(), oldUser);
        return oldUser;
    }

    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }


    public boolean userExistsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
