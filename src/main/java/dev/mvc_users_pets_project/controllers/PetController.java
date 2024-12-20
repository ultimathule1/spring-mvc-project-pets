package dev.mvc_users_pets_project.controllers;

import dev.mvc_users_pets_project.model.PetDto;
import dev.mvc_users_pets_project.services.PetService;
import dev.mvc_users_pets_project.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetController {
    private final PetService petService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping("/pets/add")
    public PetDto createdPet(@RequestBody PetDto petDto) {
        var savedPet = petService.savePet(petDto);
        userService.addPetToUser(savedPet);
        log.info("Get request: (Get) Saved pet: {}", savedPet);
        return petDto;
    }

    @GetMapping("/pets/{id}")
    public PetDto getPetById(@PathVariable Long id) {
        var foundPet = petService.findPetById(id);
        log.info("Get request: (Get) Get pet by id: {}", foundPet);
        return foundPet;
    }

    @GetMapping("/pets")
    public List<PetDto> getAllPets() {
        List<PetDto> pets = petService.getAllPets();
        log.info("Get request: (Get) All pets: {}", pets);
        return pets;
    }
}