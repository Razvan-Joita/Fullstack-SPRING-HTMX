package org.example.service;

import org.example.model.ERole;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @Test
    void saveUserValid(){
        List<User> users = List.of(
                new User(1, "Radu", "Valentin", "user1@example.com", "12345", ERole.STD)
        );
        when(userRepository.findAll()).thenReturn(users);

        User validUserToSave = new User(2, "Radu", "Valentin", "user2@example.com", "Yth67#890afa", ERole.STD);
        assertTrue(userService.validateEmail(validUserToSave.getEmail()));
        assertTrue(userService.validatePassword(validUserToSave.getPassword()));
        assertTrue(userService.validateNames(validUserToSave.getFirstname()));
        assertTrue(userService.validateNames(validUserToSave.getLastname()));

        when(userRepository.save(validUserToSave)).thenReturn(validUserToSave);
        User savedUser = userService.saveUser(validUserToSave);

        assertNotNull(savedUser);
        verify(userRepository,times(1)).save(validUserToSave);
    }

    @Test
    void saveUserInvalid(){
        User invalidUserToSave = new User(1, "Radu", "Valentin", "user1@example.com", "12345", ERole.STD);
        assertFalse(userService.validatePassword(invalidUserToSave.getPassword()));

        User savedUser = userService.saveUser(invalidUserToSave);
        verify(userRepository, never()).save(any());
        assertNull(savedUser);
    }

    @Test
    void validateEmail() {
        List<User> users = Arrays.asList(
                new User(1, "Radu", "Valentin", "user1@example.com", "12345", ERole.STD),
                new User(2, "Mihai", "Augustin", "user2@example.com", "12345", ERole.ADM)
        );
        when(userRepository.findAll()).thenReturn(users);

        assertFalse(userService.validateEmail(null));
        assertTrue(userService.validateEmail("donationmanager@yahoo.com"));
        assertFalse(userService.validateEmail("_@_"));
        assertFalse(userService.validateEmail("boss_tudor@"));
        assertFalse(userService.validateEmail("boss_tudor@ya"));
        assertTrue(userService.validateEmail("boss_tudor@yahoo.com"));
        assertFalse(userService.validateEmail("boss_tudor@yahoo."));
        assertFalse(userService.validateEmail("user1@example.com"));

        verify(userRepository, times(7)).findAll();

    }

    @Test
    void validateNames() {
        assertFalse(userService.validateNames(null));
        assertTrue(userService.validateNames("Radu"));
        assertFalse(userService.validateNames("123iwe"));
        assertFalse(userService.validateNames("ra du"));
        assertFalse(userService.validateNames("S"));
    }

    @Test
    void validatePassword() {
        assertFalse(userService.validatePassword(null));
        assertFalse(userService.validatePassword("1234567"));
        assertFalse(userService.validatePassword("abafvahjfvwefwef"));
        assertFalse(userService.validatePassword("Rbuikafafwe"));
        assertFalse(userService.validatePassword("        "));
        assertFalse(userService.validatePassword("1Aa@"));
        assertFalse(userService.validatePassword("Rhvjaj12"));
        assertTrue(userService.validatePassword("Rdi%12fjkw"));
    }

}
