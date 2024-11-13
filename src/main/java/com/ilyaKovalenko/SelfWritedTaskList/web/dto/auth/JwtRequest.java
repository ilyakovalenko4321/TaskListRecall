package com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull(message = "Username field must be not empty")
    private String username;

    @NotNull(message = "Password field must be noe empty")
    private String password;

}
