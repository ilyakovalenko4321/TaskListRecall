package com.ilyaKovalenko.SelfWritedTaskList.domain.Task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
public class Task implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String title;
    private String description;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDateTime expirationDate;

}
