package org.example.controller;

import org.example.model.ERole;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Test
    void saveUserValid() throws Exception {
        User validUserToSave = new User(1, "Radu", "Valentin", "user2@example.com", "Yth67#890afa", ERole.STD);

        doReturn(validUserToSave).when(userService).saveUser(any(User.class));

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc.perform(post("/users/register")
                        .param("lastname", validUserToSave.getLastname())
                        .param("firstname", validUserToSave.getFirstname())
                        .param("email", validUserToSave.getEmail())
                        .param("password", validUserToSave.getPassword())
                        .param("role", validUserToSave.getRole().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully created"))
                .andReturn().getResponse().getContentAsString();

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void saveUserInvalid() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc.perform(post("/users/register")
                        .param("lastname", "Radu")
                        .param("firstname", "Valentin")
                        .param("email", "user1@example.com")
                        .param("password", "12345")
                        .param("role", "STD"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid user"))
                .andReturn().getResponse().getContentAsString();
    }

}
