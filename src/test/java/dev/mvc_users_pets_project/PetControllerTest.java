package dev.mvc_users_pets_project;

import dev.mvc_users_pets_project.controllers.PetController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class PetControllerTest {

    @Autowired
    private PetController petController;

    @Autowired
    private MockMvc mockMvc;
}
