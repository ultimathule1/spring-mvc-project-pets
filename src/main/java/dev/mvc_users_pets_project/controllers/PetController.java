package dev.mvc_users_pets_project.controllers;

import dev.mvc_users_pets_project.model.pets.PetDto;
import dev.mvc_users_pets_project.model.pets.PetDtoConverter;
import dev.mvc_users_pets_project.services.PetService;
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
@RequestMapping("/pets")
public class PetController {
    private final PetService petService;
    private final PetDtoConverter petDtoConverter;
    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    public PetController(PetService petService, PetDtoConverter petDtoConverter) {
        this.petService = petService;
        this.petDtoConverter = petDtoConverter;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(
            @Valid @RequestBody PetDto petDto) {
        log.info("Received request: (POST) Save pet: {}", petDto);
        var savedPet = petService.savePet(petDtoConverter.toEntity(petDto));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petDtoConverter.toDto(savedPet));
    }

    @GetMapping("/{id}")
    public PetDto getPetById(@PathVariable Long id) {
        log.info("Received request: (GET) Get pet by id: {}", id);
        return petDtoConverter.toDto(petService.findPetById(id));
    }

    @GetMapping
    public List<PetDto> getAllPets() {
        log.info("Received request: (GET) get all pets");
        return petService.getAllPets()
                .stream()
                .map(petDtoConverter::toDto)
                .toList();
    }

    @PutMapping("/{id}")
    public PetDto updatePet(
            @PathVariable("id") Long id,
            @Valid @RequestBody PetDto petDto) {
        log.info("Received request: (PUT) Update pet: {} with id {}", petDto, id);
        var updatePet = petService.updatePet(id, petDtoConverter.toEntity(petDto));
        return petDtoConverter.toDto(updatePet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long id
    ) {
        log.info("Received request: (DELETE) Delete pet id: {}", id);
        petService.deletePet(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}