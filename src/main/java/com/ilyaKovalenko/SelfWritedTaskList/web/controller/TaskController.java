package com.ilyaKovalenko.SelfWritedTaskList.web.controller;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.service.TaskService;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.task.TaskDto;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.validation.OnUpdate;
import com.ilyaKovalenko.SelfWritedTaskList.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private TaskService taskService;

    private TaskMapper taskMapper;


    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getTaskById(@PathVariable Long id){
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto){
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void delete(@PathVariable Long id){
        taskService.delete(id);
    }

}
