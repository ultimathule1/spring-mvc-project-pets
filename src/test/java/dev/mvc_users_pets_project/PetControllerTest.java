package dev.mvc_users_pets_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mvc_users_pets_project.model.pets.Pet;
import dev.mvc_users_pets_project.model.pets.PetDto;
import dev.mvc_users_pets_project.model.users.User;
import dev.mvc_users_pets_project.model.users.UserDto;
import dev.mvc_users_pets_project.model.users.UserDtoConverter;
import dev.mvc_users_pets_project.services.PetService;
import dev.mvc_users_pets_project.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDtoConverter userDtoConverter;

    private final String BASE_URL = "/pets";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreatePet() throws Exception {

        UserDto createdUser = createAndSaveTestUser();

        PetDto petDto = new PetDto(
                null,
                "Fluffy",
                createdUser.id()
        );

        String petJson = objectMapper.writeValueAsString(petDto);

        String createdPetJson = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
                )
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto createdPet = objectMapper.readValue(createdPetJson, PetDto.class);

        Assertions.assertEquals(petDto.name(), createdPet.name());
        Assertions.assertEquals(petDto.userId(), createdPet.userId());
        Assertions.assertNotNull(createdPet.id());
    }

    @Test
    void getPetById() throws Exception {
        UserDto createdUser = createAndSaveTestUser();
        Pet pet = new Pet(
                null,
                "Kesha",
                createdUser.id()
        );
        Long idForPet = petService.savePet(pet).id();

        String gotPetJson = mockMvc.perform(get(BASE_URL + "/" + idForPet))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto gotPet = objectMapper.readValue(gotPetJson, PetDto.class);

        Assertions.assertEquals(pet.name(), gotPet.name());
        Assertions.assertEquals(pet.userId(), gotPet.userId());
        Assertions.assertEquals(gotPet.id(), idForPet);
    }

    @Test
    void deletePetById() throws Exception {
        UserDto createdUser = createAndSaveTestUser();
        Pet pet = new Pet(
                null,
                "Kusha",
                createdUser.id()
        );
        Pet createdPet = petService.savePet(pet);
        int sizeBeforeDelete = petService.getAllPets().size();

        String deletedPetJson = mockMvc.perform(delete(BASE_URL + "/delete?id=" + createdPet.id()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto deletedPet = objectMapper.readValue(deletedPetJson, PetDto.class);

        Assertions.assertEquals(pet.name(), deletedPet.name());
        Assertions.assertEquals(pet.userId(), deletedPet.userId());
        Assertions.assertEquals(sizeBeforeDelete - 1, petService.getAllPets().size());
    }

    private UserDto createAndSaveTestUser() {
        UserDto userDto = new UserDto(
                null,
                "some user",
                "ya@rumbler.ru",
                44
        );

        User savedUser = userService.saveUser(userDtoConverter.toEntity(userDto));
        return userDtoConverter.toDto(savedUser);
    }
}