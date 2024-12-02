package com.ilyaKovalenko.SelfWritedTaskList.service;

import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtRequest;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtResponse;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.user.UserDto;

public interface AuthService {

    JwtResponse refresh(String request);

    JwtResponse login(JwtRequest request);

    UserDto confirmEmail(JwtRequest request);

    void logout(String token);


}
