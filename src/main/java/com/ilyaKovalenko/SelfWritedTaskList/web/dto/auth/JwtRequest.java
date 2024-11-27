package com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull
    private String username;

    @NotNull(message = "Password field must be noe empty")
    private String password;

}
