package com.ilyaKovalenko.SelfWritedTaskList.domain.User;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    private String name;

    @NotNull
    @Column(name = "username", unique = true, length = 1024)
    private String username;

    private String password;
    @Transient
    private String passwordConfirmation;

    private String email;

    private String phoneNumber;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @CollectionTable(name = "users_tasks")
    @OneToMany
    @JoinColumn(name = "id")
    private List<Task> tasks;

}


