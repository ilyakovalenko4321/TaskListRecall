package com.ilyaKovalenko.SelfWritedTaskList.repository;

import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query(value = """
           SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username
           """)
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = """
            SELECT EXISTS(
            SELECT 1 FROM users_tasks
            WHERE user_id = :userId
            AND task_id = :taskId
            )""", nativeQuery = true)
    boolean isTaskOwner(@Param("userId") Long userId, @Param("taskId") Long taskId);


}
