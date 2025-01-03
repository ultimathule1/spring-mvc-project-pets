package dev.mvc_users_pets_project.model.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mvc_users_pets_project.model.pets.PetDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        @Null
        Long id,

        @NotBlank(message = "firstName cannot be empty")
        @Size(min = 1, max = 50, message = "incorrect firstname size")
        @JsonProperty("name")
        String firstName,

        @NotNull(message = "user email is null")
        @Email(message = "incorrect email")
        String email,

        @NotNull(message = "user age is null")
        @Min(value = 14, message = "The user is too young")
        @Max(value = 130, message = "The user too old")
        Integer age,

        @NonNull
        List<PetDto> pets
) {
    public UserDto(Long id, String firstName, String email, Integer ages) {
        this(id, firstName, email, ages, new CopyOnWriteArrayList<>());
    }
}