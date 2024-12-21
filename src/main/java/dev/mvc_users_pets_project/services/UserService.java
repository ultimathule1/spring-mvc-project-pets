package dev.mvc_users_pets_project.services;

import dev.mvc_users_pets_project.Pet;
import dev.mvc_users_pets_project.model.PetDto;
import dev.mvc_users_pets_project.model.UserDto;
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
    Map<Long, UserDto> users;
    AtomicLong idCounter;
    private final String USER_NOT_FOUND= "User not found";

    public UserService() {
        users = new ConcurrentHashMap<>();
        idCounter = new AtomicLong(0);
    }

    public UserDto saveUser(UserDto user) {
        var newId = idCounter.incrementAndGet();
        var newUser = new UserDto(
                newId,
                user.firstName(),
                user.email(),
                user.age()
        );

        users.put(newId, newUser);
        return newUser;
    }

    public UserDto findUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND));
    }

    public List<UserDto> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public UserDto updateUser(Long id, UserDto userToUpdate) {
        if (users.get(id) == null) {
            throw new NoSuchElementException(USER_NOT_FOUND);
        }

        var newUser = new UserDto(
                id,
                userToUpdate.firstName(),
                userToUpdate.email(),
                userToUpdate.age(),
                userToUpdate.pets()
        );

        users.put(id, newUser);
        return newUser;
    }

    public UserDto deleteUser(Long id) {
        UserDto removedUser = Optional.ofNullable(users.remove(id))
                .orElseThrow(() -> new NoSuchElementException(USER_NOT_FOUND));

        removedUser.pets().clear();
        return removedUser;
    }

    public void addPetToUser(PetDto petToAdd) {
        var userId = petToAdd.userId();
        if(users.get(userId) == null) {
            throw new NoSuchElementException(USER_NOT_FOUND);
        }
        users.get(userId).pets().add(petToAdd);
    }

    public void deletePetFromUser(PetDto petToRemove) {
        if (petToRemove.userId() == null) {
            throw new NoSuchElementException(USER_NOT_FOUND);
        }
        boolean removed = users.get(petToRemove.userId()).pets().remove(petToRemove);
        if (!removed) {
            throw new NoSuchElementException("Pet not found");
        }
    }

    public void changePetToUser(PetDto updatePet) {
        var userId = updatePet.userId();
        if (users.get(userId) == null) {
            throw new NoSuchElementException(USER_NOT_FOUND);
        }

        for (UserDto user : users.values()) {
            user.pets().removeIf(pet -> pet.id().equals(updatePet.id()));
        }

        users.get(userId).pets().add(updatePet);
    }
}