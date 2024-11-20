package com.ilyaKovalenko.SelfWritedTaskList.web.controller;

import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.service.AuthService;
import com.ilyaKovalenko.SelfWritedTaskList.service.UserService;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtRequest;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtResponse;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.user.UserDto;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.validation.OnCreate;
import com.ilyaKovalenko.SelfWritedTaskList.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    //ToDo: Add ability to chose by what you will be login
    @PostMapping(path = "/login/username")
    public JwtResponse loginByUsername(@Validated @RequestBody JwtRequest loginRequest){
        return authService.loginByUsername(loginRequest);
    }

    @PostMapping("/login/email")
    public JwtResponse loginByEmail(@Validated @RequestBody JwtRequest loginRequest){
        return authService.loginByEmail(loginRequest);
    }

    @PostMapping("/login/phone_number")
    public JwtResponse loginByPhoneNumber(@Validated @RequestBody JwtRequest loginRequest){
        return authService.loginByPhoneNumber(loginRequest);
    }

    //ToDo: Add a email confirmation
    @PostMapping(path = "/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto dto){
        User user = userMapper.toEntity(dto);
        User newUser = userService.create(user);
        return userMapper.toDto(newUser);
    }

    @PostMapping(path = "/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken){
        return authService.refresh(refreshToken);
    }

}
