package dev.mvc_users_pets_project.model.pets;

public record Pet(
        Long id,
        String name,
        Long userId
) {
}