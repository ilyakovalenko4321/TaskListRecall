package com.ilyaKovalenko.SelfWritedTaskList.repository;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT * FROM tasks t
            JOIN users_tasks ut ON ut.task_id = t.id
            WHERE ut.user_id = :userId
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = """
            INSERT INTO users_tasks (user_id, task_id)
            VALUES (:userId, :taskId)
            """, nativeQuery = true)
    void assignTaskToUser(Long userId, Long taskId);

    @Modifying
    @Query(value = """
            INSERT INTO tasks_images (task_id, image)
            VALUES (:id, :fileName)
            """, nativeQuery = true)
    void addImage(@Param("id") Long id, @Param("fileName") String fileName);

    @Query(value = """
           SELECT title, description
           FROM tasks t
           WHERE t.expiration_date between :start and :end
           """, nativeQuery = true)
    List<Task> findAllSoonTask(@Param("start") Timestamp start, @Param("end") Timestamp end);
}
