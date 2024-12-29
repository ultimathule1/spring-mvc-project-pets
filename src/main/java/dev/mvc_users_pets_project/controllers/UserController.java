package dev.mvc_users_pets_project.controllers;

import dev.mvc_users_pets_project.model.users.User;
import dev.mvc_users_pets_project.model.users.UserDto;
import dev.mvc_users_pets_project.model.users.UserDtoConverter;
import dev.mvc_users_pets_project.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    public UserController(UserService userService, UserDtoConverter userDtoConverter) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid UserDto userDto
    ) {
        log.info("Received request: (POST) Creating user: {}", userDto);
        User createdUser = userService.saveUser(userDtoConverter.toEntity(userDto));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userDtoConverter.toDto(createdUser));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(
            @PathVariable long id
    ) {
        log.info("Received request: (GET) Get user: {}", id);
        var user = userService.findUserById(id);
        return userDtoConverter.toDto(user);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Received request: (GET) Get all users");
        return userService.getAllUsers()
                .stream()
                .map(userDtoConverter::toDto)
                .toList();
    }

    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable("id") Long id,
            @RequestBody @Valid UserDto userDto) {
        log.info("Received request: (PUT) Update user: {}", userDto);
        User updatedUser = userService.updateUser(id, userDtoConverter.toEntity(userDto));
        return userDtoConverter.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        log.info("Received request: (DELETE) Delete user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
