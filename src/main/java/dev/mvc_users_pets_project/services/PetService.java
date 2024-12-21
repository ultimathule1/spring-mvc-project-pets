package dev.mvc_users_pets_project.services;

import dev.mvc_users_pets_project.model.PetDto;
import dev.mvc_users_pets_project.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {
    private Map<Long, PetDto> pets;
    private AtomicLong id;
    private final String PET_NOT_FOUND = "Pet not found";

    public PetService() {
        this.pets = new ConcurrentHashMap<>();
        this.id = new AtomicLong(0);
    }

    public PetDto savePet(PetDto petDto) {
        var newId = id.incrementAndGet();

        var newPet = new PetDto(
                newId,
                petDto.name(),
                petDto.userId()
        );

        pets.put(newId, newPet);
        return newPet;
    }

    public PetDto findPetById(long id) {
        return Optional.ofNullable(pets.get(id))
                .orElseThrow(() -> new IllegalArgumentException(PET_NOT_FOUND));
    }

    public List<PetDto> getAllPets() {
        return pets.values().stream().toList();
    }

    public PetDto updatePet(Long id, PetDto petDto) {
        if (pets.get(id) == null) {
            throw new IllegalArgumentException(PET_NOT_FOUND);
        }
        var newPet = new PetDto(
                id,
                petDto.name(),
                petDto.userId()
        );

        pets.put(newPet.id(), newPet);

        return newPet;
    }

    public PetDto deletePet(long id) {
        if (pets.get(id) == null) {
            throw new IllegalArgumentException(PET_NOT_FOUND);
        }
        return pets.remove(id);
    }

    public void deletePetsByUserId(long userId) {
        pets.values().stream()
                .filter(p -> p.userId() == userId)
                .forEach((p) -> pets.remove(p.id()));
    }

    public void updatePetsByUser(UserDto updatedUser) {
        if (updatedUser == null || updatedUser.id() == null) {
            throw new IllegalArgumentException("User Not Found");
        }

        for (PetDto pet : updatedUser.pets()) {
            if (pet.id() == null) {
                throw new IllegalArgumentException("Updated User has a pet with null id");
            }
        }

        pets.values().stream()
                .filter(p -> p.userId().equals(updatedUser.id()))
                .forEach(p -> pets.remove(p.id()));

        updatedUser.pets()
                .forEach(p -> pets.put(p.id(), p));
    }
}