package dev.mvc_users_pets_project.controllers;

import dev.mvc_users_pets_project.model.UserDto;
import dev.mvc_users_pets_project.services.PetService;
import dev.mvc_users_pets_project.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final PetService petService;

    public UserController(UserService userService, PetService petService) {
        this.userService = userService;
        this.petService = petService;
    }

    @PostMapping("/users/add")
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto userDto
    ) {
        log.info("Get request: (Post) Creating user: {}", userDto);
        UserDto createdUser = userService.saveUser(userDto);

        return ResponseEntity
                .status(201)
                .body(createdUser);
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(
            @PathVariable @Valid long id
    ) {
        var user = userService.findUserById(id);
        log.info("Get request: (Get) Get user: {}", user);
        return user;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        List<UserDto> getAllUsers = userService.getAllUsers();
        log.info("Get request: (Get) Get all users: {}", getAllUsers);
        return getAllUsers;
    }

    @PutMapping("/users/{id}")
    public UserDto updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        log.info("Get request: (Put) Updated user: {}", updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/users/delete")
    public UserDto deleteUser(
            @RequestParam Long id
    ) {
        UserDto userDto = userService.deleteUser(id);
        petService.deletePetsByUserId(userDto.id());
        log.info("Get request: (Delete) Deleted user: {}", id);
        return userDto;
    }
}
