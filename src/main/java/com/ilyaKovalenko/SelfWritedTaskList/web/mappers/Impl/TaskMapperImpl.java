package com.ilyaKovalenko.SelfWritedTaskList.web.mappers.Impl;


import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.task.TaskDto;
import com.ilyaKovalenko.SelfWritedTaskList.web.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component("taskMapperImpl")
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDto toDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setId( task.getId() );
        taskDto.setTitle( task.getTitle() );
        taskDto.setDescription( task.getDescription() );
        taskDto.setStatus( task.getStatus() );
        taskDto.setExpirationDate( task.getExpirationDate() );

        return taskDto;
    }

    @Override
    public List<TaskDto> toDto(List<Task> tasks) {
        if ( tasks == null ) {
            return null;
        }

        List<TaskDto> list = new ArrayList<TaskDto>( tasks.size() );
        for ( Task task : tasks ) {
            list.add( toDto( task ) );
        }

        return list;
    }

    @Override
    public Task toEntity(TaskDto dto) {
        if ( dto == null ) {
            return null;
        }

        Task task = new Task();

        task.setId( dto.getId() );
        task.setTitle( dto.getTitle() );
        task.setDescription( dto.getDescription() );
        task.setStatus( dto.getStatus() );
        task.setExpirationDate( dto.getExpirationDate() );

        return task;
    }
}

