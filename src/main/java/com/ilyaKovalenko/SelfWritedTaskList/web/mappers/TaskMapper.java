package com.ilyaKovalenko.SelfWritedTaskList.web.mappers;


import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.task.TaskDto;

import java.util.List;

public interface TaskMapper {

    TaskDto toDto(Task task);
    List<TaskDto> toDto(List<Task> task);
    Task toEntity(TaskDto dto);

}
