package com.ilyaKovalenko.SelfWritedTaskList.web.mappers;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.TaskImage;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.task.TaskImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}
