package dev.mvc_users_pets_project.services;

import dev.mvc_users_pets_project.model.pets.Pet;
import dev.mvc_users_pets_project.model.users.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {
    private final UserService userService;
    private final AtomicLong id;
    private final static String PET_NOT_FOUND = "Pet not found";

    public PetService(UserService userService) {
        this.userService = userService;
        this.id = new AtomicLong(0);
    }

    public Pet savePet(Pet pet) {
        var newId = id.incrementAndGet();

        var newPet = new Pet(
                newId,
                pet.name(),
                pet.userId()
        );

        savePetToUser(newPet);
        return newPet;
    }

    public Pet findPetById(long id) {
        return userService.getAllUsers()
                .stream()
                .flatMap(u -> u.pets().stream())
                .filter(u -> u.id() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(PET_NOT_FOUND));
    }

    public List<Pet> getAllPets() {
        return userService.getAllUsers()
                .stream()
                .flatMap(u -> u.pets().stream())
                .toList();
    }

    public Pet updatePet(Long id, Pet pet) {
        Pet updatedPet = new Pet(
                id,
                pet.name(),
                pet.userId()
        );
        deletePet(id);
        savePetToUser(updatedPet);
        return updatedPet;
    }

    public Pet deletePet(long id) {
        User user = userService.getAllUsers().stream()
                .filter(u -> u.pets().stream().anyMatch(pet -> pet.id() == id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(PET_NOT_FOUND));

        Pet petToDelete = user.pets().stream()
                .filter(u -> u.id() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(PET_NOT_FOUND));

        user.pets().remove(petToDelete);
        return petToDelete;
    }

    private void savePetToUser(Pet pet) {
        userService.findUserById(pet.userId()).pets().add(pet);
    }
}