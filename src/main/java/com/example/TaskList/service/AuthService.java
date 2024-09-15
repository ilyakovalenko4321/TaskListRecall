package com.example.TaskList.service;

import com.example.TaskList.web.dto.auth.JwtRequest;
import com.example.TaskList.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);



}
