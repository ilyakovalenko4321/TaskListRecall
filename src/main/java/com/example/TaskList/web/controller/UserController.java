package com.example.TaskList.web.controller;

import com.example.TaskList.domain.task.Task;
import com.example.TaskList.domain.user.User;
import com.example.TaskList.service.TaskService;
import com.example.TaskList.service.UserService;
import com.example.TaskList.web.dto.task.TaskDto;
import com.example.TaskList.web.dto.user.UserDto;
import com.example.TaskList.web.dto.validation.OnCreate;
import com.example.TaskList.web.dto.validation.OnUpdate;
import com.example.TaskList.web.mappers.TaskMapper;
import com.example.TaskList.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    private final TaskMapper taskMapper;
    private final TaskService taskService;


    //Do not understand what is RequestBody for
    @PutMapping
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto dto){
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id){
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskDto> getTasksByUserId(@PathVariable Long id){
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    public TaskDto createTask(@PathVariable Long id,
                              @Validated(OnCreate.class) @RequestBody TaskDto dto){
        Task task = taskMapper.toEntity(dto);
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }

}
