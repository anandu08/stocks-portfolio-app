package com.portfolioapp.stocks.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.portfolioapp.stocks.exception.UserNotFoundException;
import com.portfolioapp.stocks.model.User;
import com.portfolioapp.stocks.repository.UserRepo;
import com.portfolioapp.stocks.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testSaveUser() {
        User user = new User();
        userService.saveUser(user);
        verify(userRepo).save(user);
    }

    @Test
    void testFindUserById_success() {
        Long userId = 1L;
        User user = new User();
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserById(userId);

        assertTrue(foundUser.isPresent());
        assertSame(user, foundUser.get());
    }

    @Test
    void testFindUserById_notFound() {
        Long userId = 2L;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userId));
    }

    @Test
    void testFindUserByEmail_success() {
        String email = "test@example.com";
        User user = new User();
        when(userRepo.findByEmail(email)).thenReturn(user);

        User foundUser = userService.findUserByEmail(email);

        assertSame(user, foundUser);
    }

    @Test
    void testFindUserByEmail_notFound() {
        String email = "invalid@example.com";
        when(userRepo.findByEmail(email)).thenReturn(null);

        assertNull(userService.findUserByEmail(email));
    }
}
