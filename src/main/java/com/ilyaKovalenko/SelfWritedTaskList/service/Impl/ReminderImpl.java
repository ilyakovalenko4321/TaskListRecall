package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Mail.MailType;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.service.MailService;
import com.ilyaKovalenko.SelfWritedTaskList.service.Reminder;
import com.ilyaKovalenko.SelfWritedTaskList.service.TaskService;
import com.ilyaKovalenko.SelfWritedTaskList.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {

    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private final Duration DURATIONS = Duration.ofHours(1);

    @Scheduled(cron = "0 0 * * * *")
    @Override
    public void remindForTask() {
        List<Task> tasks = taskService.getAllSoonTasks(DURATIONS);
        tasks.forEach(task -> {
            User user = userService.getTaskAuthor(task.getId());
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            mailService.sendEmail(user, MailType.REMINDER, properties, "");
        });

    }

}
