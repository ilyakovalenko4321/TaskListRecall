package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;


import com.ilyaKovalenko.SelfWritedTaskList.config.TestConfig;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.ResourceNotFoundException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Status;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.TaskImage;
import com.ilyaKovalenko.SelfWritedTaskList.repository.TaskRepository;
import com.ilyaKovalenko.SelfWritedTaskList.service.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.getById(id);
        Mockito.verify(taskRepository).findById(id);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getAllByUserId() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(new Task());
        }
        Mockito.when(taskRepository.findAllByUserId(userId))
                .thenReturn(tasks);
        List<Task> testTasks = taskService.getAllByUserId(userId);
        Mockito.verify(taskRepository).findAllByUserId(userId);
        Assertions.assertEquals(tasks, testTasks);
    }


    @Test
    void update() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        task.setStatus(Status.DONE);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void updateWithEmptyStatus() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, testTask);

    }

    @Test
    void create() {
        Long userId = 1L;
        Long taskId = 1L;
        Task task = new Task();
        Mockito.doAnswer(invocation -> {
                    Task savedTask = invocation.getArgument(0);
                    savedTask.setId(taskId);
                    return savedTask;
                })
                .when(taskRepository).save(task);
        Task testTask = taskService.create(task, userId);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertNotNull(testTask.getId());
        Mockito.verify(taskRepository).assignTaskToUser(userId, task.getId());
    }

    @Test
    void delete() {
        Long id = 1L;
        taskService.delete(id);
        Mockito.verify(taskRepository).deleteById(id);
    }

    @Test
    void uploadImage(){
        Long id = 1L;
        String fileName = "imageName";
        TaskImage taskImage = new TaskImage();
        Mockito.when(imageService.upload(taskImage))
                .thenReturn(fileName);
        taskService.uploadImage(id, taskImage);
        Mockito.verify(taskRepository).addImage(id, fileName);
    }

}
