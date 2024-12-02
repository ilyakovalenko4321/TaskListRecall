package com.ilyaKovalenko.SelfWritedTaskList.domain.Token;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "token_sessions_access")
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    // Другие поля и геттеры/сеттеры
}
