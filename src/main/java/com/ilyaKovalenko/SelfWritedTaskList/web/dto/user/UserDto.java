package com.ilyaKovalenko.SelfWritedTaskList.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.validation.OnCreate;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

@Data
public class UserDto {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Name length must be less then 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Username must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Username length must be less then 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @NotNull
    @Email
    private String email;

    @NumberFormat
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnUpdate.class, OnCreate.class})
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Confirmation must be not null", groups = OnCreate.class)
    private String passwordConfirmation;

}
