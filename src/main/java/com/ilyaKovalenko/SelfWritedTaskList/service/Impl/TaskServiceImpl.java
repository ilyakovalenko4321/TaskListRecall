package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.ResourceNotFoundException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Status;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.TaskImage;
import com.ilyaKovalenko.SelfWritedTaskList.repository.TaskRepository;
import com.ilyaKovalenko.SelfWritedTaskList.service.ImageService;
import com.ilyaKovalenko.SelfWritedTaskList.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TasService::getById", key = "#id")
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Unable to find task by this id"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }


    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task create(Task task, Long userId) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }

        if (task.getExpirationDate() == null) {
            task.setExpirationDate(LocalDateTime.now().plus(Duration.ofHours(24)));
        }

        taskRepository.save(task);

        taskRepository.assignTaskToUser(userId, task.getId());

        return task;
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);

        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void uploadImage(Long id, TaskImage image) {

        String fileName = imageService.upload(image);
        taskRepository.addImage(id, fileName);

    }

    @Override
    public List<Task> getAllSoonTasks(Duration duration) {
        LocalDateTime now = LocalDateTime.now();
        return taskRepository.findAllSoonTask(Timestamp.valueOf(now), Timestamp.valueOf(now.plus(duration)));
    }

    @Override
    @Transactional
    public void deleteAllByUserId(Long id) {
        List<Task> tasks = getAllByUserId(id);
        List<Long> tasksId = tasks.stream().map(Task::getId).toList();
        taskRepository.deleteAllByIdInBatch(tasksId);
        System.out.println("ddd");
    }
}
