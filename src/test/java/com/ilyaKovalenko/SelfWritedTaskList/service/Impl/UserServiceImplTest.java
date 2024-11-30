package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;


import com.ilyaKovalenko.SelfWritedTaskList.config.TestConfig;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.ResourceNotFoundException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.Role;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getById_withExistingId_returnsUser() {
        // Arrange
        Long id = 1L;
        User user = new User();
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        User testUser = userService.getById(id);

        // Assert
        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getById_withNonExistingId_throwsException() {
        // Arrange
        Long nonExistingId = 2L;

        Mockito.when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getById(nonExistingId));
        Mockito.verify(userRepository).findById(nonExistingId);
    }

    @Test
    void getByUsername_withExistingUsername_returnsUser() {
        // Arrange
        String existingUsername = "testUsername";
        User user = new User();
        user.setUsername(existingUsername);

        Mockito.when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(user));

        // Act
        User testUser = userService.getByUsername(existingUsername);

        // Assert
        Mockito.verify(userRepository).findByUsername(existingUsername);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByUsername_withNonExistingUsername_throwsException() {
        // Arrange
        String nonExistingUsername = "username";

        Mockito.when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getByUsername(nonExistingUsername));
        Mockito.verify(userRepository).findByUsername(nonExistingUsername);
    }


    @Test
    void getByEmail_userExists() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User retrievedUser = userService.getByEmail(email);

        // Assert
        Mockito.verify(userRepository).findByEmail(email);
        Assertions.assertEquals(user, retrievedUser);
    }

    @Test
    void getByEmail_userNotExists() {
        // Arrange
        String email = "nonexistent@example.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getByEmail(email));
        Mockito.verify(userRepository).findByEmail(email);
    }

    @Test
    void getByPhoneNumber_userExists() {
        // Arrange
        String phoneNumber = "1234567890";
        User user = new User();
        user.setPhoneNumber(phoneNumber);

        Mockito.when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));

        // Act
        User retrievedUser = userService.getByPhoneNumber(phoneNumber);

        // Assert
        Mockito.verify(userRepository).findByPhoneNumber(phoneNumber);
        Assertions.assertEquals(user, retrievedUser);
    }

    @Test
    void getByPhoneNumber_userNotExists() {
        // Arrange
        String phoneNumber = "0987654321";

        Mockito.when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getByPhoneNumber(phoneNumber));
        Mockito.verify(userRepository).findByPhoneNumber(phoneNumber);
    }

    @Test
    void update_WithValidUser_EncodesPasswordAndSaves() {
        // Arrange
        User user = new User();
        user.setId(1L);
        String password = "password123";
        user.setEmail("valid@email.com");
        user.setPassword(password);
        user.setPasswordConfirmation(password);

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");

        // Настройка мока
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        User updatedUser = userService.update(user);

        // Assert
        Mockito.verify(passwordEncoder).encode(password); // Проверяем точный вызов
        Mockito.verify(userRepository).save(user); // Проверяем сохранение
        Assertions.assertEquals("encodedPassword", updatedUser.getPassword()); // Проверяем результат
    }


    @Test
    void update_WithMismatchedPasswordAndConfirmation_ThrowsException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setPassword("password123");
        user.setPasswordConfirmation("differentPassword");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");

        // Настройка мока для userRepository
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> userService.update(user));

        Assertions.assertEquals("Password and password confirmation do not match", exception.getMessage());

        // Проверяем, что методы passwordEncoder и userRepository не вызывались
        Mockito.verifyNoInteractions(passwordEncoder);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void update_WithEmptyEmail_SkipsEmailConfirmation() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("");
        String password = "password123";
        user.setPassword(password);
        user.setPasswordConfirmation(password);

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");

        // Мок репозитория, чтобы вернуть существующего пользователя
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        // Мок кодировщика паролей
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        // Мок сохранения пользователя
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        User updatedUser = userService.update(user);

        // Assert
        Mockito.verify(passwordEncoder).encode(password); // Убедиться, что пароль закодирован
        Mockito.verify(userRepository).save(user); // Проверить, что пользователь сохранен
        Assertions.assertEquals("encodedPassword", updatedUser.getPassword()); // Проверить, что пароль сохранен корректно
        Assertions.assertEquals("", updatedUser.getEmail()); // Email остался пустым
    }

    @Test
    void update_WithNullPasswordConfirmation_ThrowsException() {
        // Arrange
        User user = new User();
        user.setId(1L);
        String password = "password123";
        user.setPassword(password);
        user.setPasswordConfirmation(null); // No confirmation

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");

        // Мок репозитория для возврата существующего пользователя
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> userService.update(user));

        Assertions.assertEquals("Password and password confirmation do not match", exception.getMessage());

        // Убедиться, что ни passwordEncoder, ни userRepository не вызывались
        Mockito.verifyNoInteractions(passwordEncoder);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void isTaskOwner() {

        Long userId = 1L;
        Long taskId = 1L;

        Mockito.when(userRepository.isTaskOwner(userId, taskId)).thenReturn(true);

        boolean isOwner = userService.isTaskOwner(userId, taskId);

        Assertions.assertTrue(isOwner);
    }

    @Test
    void create() {
        String username = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        User testUser = userService.create(user);
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(Set.of(Role.ROLE_USER), testUser.getRoles());
        Assertions.assertEquals("encodedPassword",
                testUser.getPassword());
    }

    @Test
    void createWithExistingUsername() {
        String username = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User()));
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void createWithDifferentPasswords() {
        String username = "username@gmail.com";
        String password = "password1";
        String passwordConfirmation = "password2";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void isTaskOwnerWithFalse() {
        Long userId = 1L;
        Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId))
                .thenReturn(false);
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertFalse(isOwner);
    }

    @Test
    void delete() {
        Long id = 1L;
        userService.delete(id);
        Mockito.verify(userRepository).deleteById(id);
    }

}