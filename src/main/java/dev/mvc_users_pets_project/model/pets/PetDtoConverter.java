package dev.mvc_users_pets_project.model.pets;

import org.springframework.stereotype.Component;

@Component
public class PetDtoConverter {

    public PetDto toDto(Pet pet) {
        return new PetDto(
                pet.id(),
                pet.name(),
                pet.userId()
        );
    }

    public Pet toEntity(PetDto petDto) {
        return new Pet(
                petDto.id(),
                petDto.name(),
                petDto.userId()
        );
    }
}
