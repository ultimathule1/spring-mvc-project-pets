package dev.mvc_users_pets_project.controllers;

import dev.mvc_users_pets_project.model.PetDto;
import dev.mvc_users_pets_project.services.PetService;
import dev.mvc_users_pets_project.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {
    private final PetService petService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public PetDto createdPet(
            @Valid @RequestBody PetDto petDto) {
        var savedPet = petService.savePet(petDto);
        userService.addPetToUser(savedPet);
        log.info("Get request: (Get) Saved pet: {}", savedPet);
        return savedPet;
    }

    @GetMapping("/{id}")
    public PetDto getPetById(@PathVariable Long id) {
        var foundPet = petService.findPetById(id);
        log.info("Get request: (Get) Get pet by id: {}", foundPet);
        return foundPet;
    }

    @GetMapping
    public List<PetDto> getAllPets() {
        List<PetDto> pets = petService.getAllPets();
        log.info("Get request: (Get) All pets: {}", pets);
        return pets;
    }

    @PutMapping("/update/{id}")
    public PetDto updatedPet(
            @PathVariable("id") Long id,
            @Valid @RequestBody PetDto petDto) {
        var updatePet = petService.updatePet(id, petDto);
        log.error("update pet: {}", updatePet);
        userService.changePetToUser(updatePet);
        log.info("Get request: (Update) Updated pet: {}", updatePet);
        return updatePet;
    }

    @DeleteMapping("/delete")
    public PetDto deletePet(
            @RequestParam("id") Long id
    ) {
        PetDto deletedPet = petService.deletePet(id);
        userService.deletePetFromUser(deletedPet);
        log.info("Get request: (Delete) Deleted pet: {}", deletedPet);
        return deletedPet;
    }
}