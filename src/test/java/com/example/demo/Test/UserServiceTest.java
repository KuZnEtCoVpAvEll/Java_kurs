package com.example.demo.Test;

import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void testGetAllUsers() {
        User user1 = User.builder().name("John Doe").email("john@example.com").password("pass").role(Role.USER).build();
        User user2 = User.builder().name("Jane Smith").email("jane@example.com").password("pass").role(Role.USER).build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        User user1 = User.builder().name("John").email("john@example.com").password("pass").role(Role.USER).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User found = userService.getUserById(1L);
        assertNotNull(found);
        assertEquals("John", found.getName());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User found = userService.getUserById(99L);
        assertNull(found); // Або кинь виняток, якщо так реалізовано
    }

    @Test
    void testSaveUser() {
        User user = User.builder().name("Save Me").email("save@example.com").password("1234").role(Role.USER).build();

        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.saveUser(user);
        assertEquals("Save Me", saved.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        User existingUser = User.builder().id(1L).name("Old").email("old@example.com").password("pass").role(Role.USER).build();
        User updatedData = User.builder().id(1L).name("New Name").email("old@example.com").password("pass").role(Role.USER).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        existingUser.setName(updatedData.getName());

        User updated = userService.updateUser(1L, updatedData); // Якщо є такий метод
        assertEquals("New Name", updated.getName());
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId); // Якщо є такий метод
        verify(userRepository, times(1)).deleteById(userId);
    }
}
