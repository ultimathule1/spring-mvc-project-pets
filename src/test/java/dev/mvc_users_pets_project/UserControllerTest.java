package dev.mvc_users_pets_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mvc_users_pets_project.model.users.User;
import dev.mvc_users_pets_project.model.users.UserDto;
import dev.mvc_users_pets_project.model.users.UserDtoConverter;
import dev.mvc_users_pets_project.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoConverter userDtoConverter;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String BASE_URL = "/users";

    @Test
    void shouldCreateValidUser() throws Exception {
        UserDto user = new UserDto(
                null,
                "Antony",
                "rop@ya.ru",
                28
        );

        String userJson = objectMapper.writeValueAsString(user);

        String resultUserJson = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                )
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto resultUser = objectMapper.readValue(resultUserJson, UserDto.class);

        Assertions.assertEquals(user.firstName(), resultUser.firstName());
        Assertions.assertEquals(user.email(), resultUser.email());
        Assertions.assertEquals(user.email(), resultUser.email());
        Assertions.assertNotNull(resultUser.id());
    }

    @Test
    void shouldGetUserById() throws Exception {
        Long idForGet = 1L;
        UserDto user = createAndSaveTestUser();

        String resultUserJson = mockMvc.perform(get(BASE_URL + "/" + idForGet)
                )
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto resultUser = objectMapper.readValue(resultUserJson, UserDto.class);

        Assertions.assertEquals(idForGet, resultUser.id());
        Assertions.assertEquals(user.firstName(), resultUser.firstName());
        Assertions.assertEquals(user.email(), resultUser.email());
    }

    @Test
    void shouldGetUserNotFound() throws Exception {
        UserDto user = createAndSaveTestUser();

        mockMvc.perform(get(BASE_URL + "/" + 2))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        Long idForDelete = 1L;

        UserDto user = createAndSaveTestUser();

        int sizeBeforeDelete = userService.getAllUsers().size();
        String deletedUserJson = mockMvc.perform(delete(BASE_URL + "/" + idForDelete))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto resultUser = objectMapper.readValue(deletedUserJson, UserDto.class);
        Assertions.assertEquals(idForDelete, resultUser.id());
        Assertions.assertEquals(user.firstName(), resultUser.firstName());
        Assertions.assertEquals(user.email(), resultUser.email());
        Assertions.assertEquals(sizeBeforeDelete - 1, userService.getAllUsers().size());
    }

    @Test
    void shouldNotBeValidForCreateUser() throws Exception {
        UserDto user = new UserDto(
                null,
                " ",
                "asd@",
                38
        );

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
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
