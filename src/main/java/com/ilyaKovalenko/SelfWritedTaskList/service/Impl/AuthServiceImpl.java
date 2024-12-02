package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.IncorrectSecretKeyException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Token.Token;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.repository.SessionRepositoryAccess;
import com.ilyaKovalenko.SelfWritedTaskList.service.AuthService;
import com.ilyaKovalenko.SelfWritedTaskList.service.UserService;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtRequest;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtResponse;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.user.UserDto;
import com.ilyaKovalenko.SelfWritedTaskList.web.mappers.UserMapper;
import com.ilyaKovalenko.SelfWritedTaskList.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final SessionRepositoryAccess sessionRepository;

    //ToDO: создать logout для удаления user или его token

    @Transactional
    @Override
    public JwtResponse refresh(String refreshRequest) {
        return jwtTokenProvider.refreshUserToken(refreshRequest);
    }

    @Transactional
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();

        // Определяем, какой тип данных был передан (username, email, phoneNumber)
        String identifier = loginRequest.getUsername();

        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Username, email, or phone number must be provided.");
        }

        // Аутентификация
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, loginRequest.getPassword()));

        // Получаем пользователя в зависимости от типа идентификатора
        User user;
        if (identifier.contains("@")) {  // Проверяем, если это email
            user = userService.getByEmail(identifier);
        } else if (identifier.matches("[0-9]+")) {  // Проверяем, если это номер телефона
            user = userService.getByPhoneNumber(identifier);
        } else {  // Предполагаем, что это username
            user = userService.getByUsername(identifier);
        }

        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername());


        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setRefreshToken(refreshToken);


        Token accessTokenEntity = new Token();
        accessTokenEntity.setToken(accessToken);
        sessionRepository.save(accessTokenEntity);

        Token refreshTokenEntity = new Token();
        refreshTokenEntity.setToken(refreshToken);
        sessionRepository.save(refreshTokenEntity);
        return jwtResponse;
    }

    @Override
    public UserDto confirmEmail(JwtRequest request) {

        String identifier = request.getUsername();
        String attemptAccessKey = request.getConfirmationCode();
        User user;
        if (identifier.contains("@")) {  // Проверяем, если это email
            user = userService.getByEmail(identifier);
        } else if (identifier.matches("[0-9]+")) {  // Проверяем, если это номер телефона
            user = userService.getByPhoneNumber(identifier);
        } else {  // Предполагаем, что это username
            user = userService.getByUsername(identifier);
        }

        Long id = user.getId();
        String accessKey = userService.getAccessKey(id);
        if (attemptAccessKey.equals(accessKey)) {
            userService.activateUser(id);
        } else {
            Integer remainingAttempt = userService.checkAbilityToConfirm(id);
            throw new IncorrectSecretKeyException("Invalid password. " + (remainingAttempt == 5 ? "You need to register your user again." : "Remaining attempts: " + remainingAttempt));
        }
        return userMapper.toDto(user);
    }

    @Override
    public void logout(String token){
        sessionRepository.logout(token);
    }

}
