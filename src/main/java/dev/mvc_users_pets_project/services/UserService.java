package dev.mvc_users_pets_project.services;

import dev.mvc_users_pets_project.model.pets.PetDto;
import dev.mvc_users_pets_project.model.users.User;
import dev.mvc_users_pets_project.model.users.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> users;
    private final AtomicLong idCounter;
    private final static String USER_NOT_FOUND = "User not found";

    public UserService() {
        users = new ConcurrentHashMap<>();
        idCounter = new AtomicLong(0);
    }

    public User saveUser(User user) {
        if (user.id() != null) {
            throw new IllegalArgumentException("User id should not be provided");
        }
        if (user.pets() != null && !user.pets().isEmpty()) {
            throw new IllegalArgumentException("Pets should not be provided");
        }

        var newId = idCounter.incrementAndGet();
        var newUser = new User(
                newId,
                user.firstName(),
                user.email(),
                user.age(),
                new ArrayList<>()
        );

        users.put(newId, newUser);
        return newUser;
    }

    public User findUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User updateUser(Long id, User userToUpdate) {
        if (users.get(id) == null) {
            throw new NoSuchElementException(USER_NOT_FOUND);
        }

        var newUser = new User(
                id,
                userToUpdate.firstName(),
                userToUpdate.email(),
                userToUpdate.age(),
                userToUpdate.pets()
        );

        users.put(id, newUser);
        return newUser;
    }

    public User deleteUser(Long id) {
        return Optional.ofNullable(users.remove(id))
                .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND));
    }
}