package com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
public class JwtRequest {

    private String username;

    @Email
    private String email;

    @NumberFormat
    private String phoneNumber;

    @NotNull(message = "Password field must be noe empty")
    private String password;

}
