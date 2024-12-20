package dev.mvc_users_pets_project.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PetDto (
    @Null
    Long id,

    @NotBlank(message = "The pet name cannot be empty")
    @Size(min = 1, max = 50, message = "incorrect pet name size")
    String name,

    @NotNull
    @PositiveOrZero
    Long userId
){
}