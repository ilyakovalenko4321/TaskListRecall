package com.ilyaKovalenko.SelfWritedTaskList.web.controller;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.TaskImage;
import com.ilyaKovalenko.SelfWritedTaskList.service.TaskService;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.task.TaskDto;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.task.TaskImageDto;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.validation.OnUpdate;
import com.ilyaKovalenko.SelfWritedTaskList.web.mappers.TaskImageMapper;
import com.ilyaKovalenko.SelfWritedTaskList.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskImageMapper taskImageMapper;


    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getTaskById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void uploadImage(@PathVariable Long id,
                            @Validated @ModelAttribute TaskImageDto imageDto) {
        TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(id, image);

    }

}
