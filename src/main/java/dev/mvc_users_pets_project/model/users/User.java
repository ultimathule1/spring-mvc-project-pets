package dev.mvc_users_pets_project.model.users;

import dev.mvc_users_pets_project.model.pets.Pet;

import java.util.ArrayList;
import java.util.List;

public record User(
        Long id,
        String firstName,
        String email,
        int age,
        List<Pet> pets
) {
    public User(Long id, String firstName, String email, int age) {
        this(id, firstName, email, age, new ArrayList<>());
    }
}
