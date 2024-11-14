package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.ResourceNotFoundException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Status;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.repository.TaskRepository;
import com.ilyaKovalenko.SelfWritedTaskList.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TasService::getById", key = "#id")
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find task by this id"));
    }

    //TODO: Implement caching for this method. Not by updates, but with CacheEvict, if user change smt
    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById", key = "#task.id")
    public Task create(Task task, Long userId) {
        if(task.getStatus() == null){
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        if(task.getStatus() == null){
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
}
