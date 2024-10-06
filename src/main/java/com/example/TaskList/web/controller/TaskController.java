package com.example.TaskList.web.controller;

import com.example.TaskList.domain.task.Task;
import com.example.TaskList.service.TaskService;
import com.example.TaskList.web.dto.task.TaskDto;
import com.example.TaskList.web.dto.validation.OnUpdate;
import com.example.TaskList.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    @PreAuthorize("canAccessTask(#id)")
    public TaskDto getById(@PathVariable Long id){
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PutMapping
    @PreAuthorize("canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto){
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("canAccessTask(#id)")
    public void deleteById(@PathVariable Long id){
        taskService.delete(id);
    }
}
