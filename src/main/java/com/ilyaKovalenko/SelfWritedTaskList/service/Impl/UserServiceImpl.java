package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.ResourceNotFoundException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Mail.MailType;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.Role;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.repository.UserRepository;
import com.ilyaKovalenko.SelfWritedTaskList.service.MailService;
import com.ilyaKovalenko.SelfWritedTaskList.service.TaskService;
import com.ilyaKovalenko.SelfWritedTaskList.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final TaskService taskService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final Duration duration = Duration.ofDays(1);
    private static final int MAX_ATTEMPTS_TO_CONFIRM = 5;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getById", key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found by id"));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found by username"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found by email"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new ResourceNotFoundException("User not found by phone number"));
    }

    @Override
    @Transactional
    public User create(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username must be unique");
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email must be unique");
        } else if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new IllegalStateException("Phone number must be unique");
        }

        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Set<Role> roles = Set.of(Role.ROLE_BLOCKED);
        user.setRoles(roles);

        String secretKey = generateAccessKey();
        userRepository.setConfirmation(user.getId(), Timestamp.valueOf(LocalDateTime.now().plusHours(1)), secretKey);

        mailService.sendEmail(user, MailType.REGISTRATION, new Properties(), secretKey);

        return user;
    }

    private static String generateAccessKey() {
        Random random = new Random();
        StringBuilder accessKey = new StringBuilder();

        // Генерация 12 случайных цифр
        for (int i = 0; i < 12; i++) {
            accessKey.append(random.nextInt(1, 10)); // Генерация случайной цифры от 1 до 9
        }

        return accessKey.toString();
    }


    @Override
    @Transactional
    @Caching(put = {@CachePut(value = "UserService::getById", key = "#user.id"), @CachePut(value = "UserService::getByUsername", key = "#user.username")})
    public User update(User user) {

        User existingUser = getById(user.getId());
        if (!Objects.equals(user.getEmail(), existingUser.getEmail())) {
            //ToDo add email confirmation
        }

        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation do not match");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::isTaskOwner", key = "#userId + '.' + #taskId")
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"UserService::getById", "TaskService::getById"}, key = "#id")
    public void delete(Long id) {
        taskService.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }

    @Override
    public User getTaskAuthor(Long taskId) {
        return userRepository.findUserByTaskId(taskId).orElseThrow(() -> {
            taskService.delete(taskId);
            return new ResourceNotFoundException("No user is own this task");
        });
    }

    //@Scheduled(cron = "0 0 * * * *")
    @Override
    @Transactional
    public void deleteAllSoonUnconfirmedUser() {
        userRepository.deleteAllSoonUnconfirmedUser(Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now().plus(duration)));
    }

    @Override
    public String getAccessKey(Long id) {
        return userRepository.findAccessKeyByUserId(id).orElseThrow(() -> new ResourceNotFoundException("Your account dont need confirmation"));
    }

    @Transactional
    @Override
    @Caching(put = {@CachePut(value = "UserService::getById", key = "#id")})
    public void activateUser(Long id) {
        userRepository.setUserRole(id);
        userRepository.deleteActivateUserFromUncomfired(id);
    }

    @Override
    @Transactional
    public Integer checkAbilityToConfirm(Long id) {
        // Получаем текущее количество оставшихся попыток
        Integer attempts = userRepository.findRemainingAttempts(id);

        // Увеличиваем количество попыток на 1
        attempts++;

        // Обновляем количество попыток в БД
        userRepository.changeAttemptsNumber(id, attempts);

        // Если достигнуто максимальное количество попыток (например, 5), удаляем пользователя
        if (attempts >= 5) {
            userRepository.deleteById(id);
        }

        return attempts;
    }
}
