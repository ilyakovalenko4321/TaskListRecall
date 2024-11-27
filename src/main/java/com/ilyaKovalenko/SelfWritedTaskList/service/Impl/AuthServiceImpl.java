package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;

import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.service.AuthService;
import com.ilyaKovalenko.SelfWritedTaskList.service.UserService;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtRequest;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtResponse;
import com.ilyaKovalenko.SelfWritedTaskList.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse refresh(String refreshRequest) {
        return jwtTokenProvider.refreshUserToken(refreshRequest);
    }

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

        // Формируем JWT-ответ
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));

        return jwtResponse;
    }


}
