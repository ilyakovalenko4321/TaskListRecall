package com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth;

import com.ilyaKovalenko.SelfWritedTaskList.web.dto.validation.OnConfirm;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull
    private String username;

    @NotNull(message = "Password field must be noe empty")
    private String password;

    @NotNull(groups = OnConfirm.class, message = "You must enter your confirm code from email message")
    private String confirmationCode;

}
