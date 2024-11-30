package com.ilyaKovalenko.SelfWritedTaskList.service;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.TaskImage;

import java.time.Duration;
import java.util.List;

public interface TaskService {

    Task getById(Long id);

    List<Task> getAllByUserId(Long userId);

    Task create(Task task, Long id);

    Task update(Task task);

    void delete(Long id);

    void uploadImage(Long id, TaskImage image);

    List<Task> getAllSoonTasks(Duration duration);

    void deleteAllByUserId(Long id);

}
