package com.ilyaKovalenko.SelfWritedTaskList.service;

import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtRequest;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse refresh(String request);

    JwtResponse login(JwtRequest request);

}
