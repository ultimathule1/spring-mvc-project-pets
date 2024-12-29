package dev.mvc_users_pets_project.model.users;


import dev.mvc_users_pets_project.model.pets.Pet;
import dev.mvc_users_pets_project.model.pets.PetDtoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDtoConverter {

    private final PetDtoConverter petDtoConverter;

    public UserDtoConverter(PetDtoConverter petDtoConverter) {
        this.petDtoConverter = petDtoConverter;
    }

    public UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.firstName(),
                user.email(),
                user.age(),
                user.pets().stream().map(petDtoConverter::toDto).toList()
        );
    }

    public User toEntity(UserDto userDto) {
        List<Pet> pets = userDto.pets().isEmpty() ? new ArrayList<>() :
                userDto.pets().stream().map(petDtoConverter::toEntity).toList();
        return new User(
                userDto.id(),
                userDto.firstName(),
                userDto.email(),
                userDto.age(),
                pets
        );
    }
}
