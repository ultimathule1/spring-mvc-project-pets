package dev.mvc_users_pets_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mvc_users_pets_project.model.UserDto;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String BASE_URL = "/users";

    @Test
    void shouldCreateValidUser() throws Exception {
        UserDto user = createTestUser();

        String userJson = objectMapper.writeValueAsString(user);

        String resultUserJson = mockMvc.perform(post(BASE_URL + "/add")
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
        UserDto user = createTestUser();

        userService.saveUser(user);

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
        UserDto user = createTestUser();

        userService.saveUser(user);

        mockMvc.perform(get(BASE_URL + "/" + 2))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        Long idForDelete = 1L;

        UserDto user = createTestUser();
        userService.saveUser(user);

        mockMvc.perform(delete(BASE_URL + "/delete?id=" + idForDelete))
                .andExpect(status().is(HttpStatus.OK.value()));
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

        mockMvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    private UserDto createTestUser() {
        return new UserDto(
                null,
                "Hoegaarden",
                "hug@rumbler.ru",
                18
        );
    }
}
