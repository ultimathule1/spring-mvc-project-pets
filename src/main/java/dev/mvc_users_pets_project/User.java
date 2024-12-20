package dev.mvc_users_pets_project;

import java.util.List;

public record User(
        Long id,
        String firstName,
        String email,
        Integer age,
        List<Pet> pets
) {
}
