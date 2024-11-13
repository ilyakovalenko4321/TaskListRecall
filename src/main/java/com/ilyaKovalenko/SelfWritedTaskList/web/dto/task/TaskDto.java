package com.ilyaKovalenko.SelfWritedTaskList.web.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Status;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.validation.OnCreate;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TaskDto {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Titile length must be less then 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Length(max = 255, message = "Titile length must be less then 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    private Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;

}