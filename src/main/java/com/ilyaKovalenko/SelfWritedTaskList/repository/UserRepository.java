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

    @Query(value = """
            SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email
            """)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = """
            SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.phoneNumber = :phoneNumber
            """)
    Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query( value = """
          SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :identifier OR u.email = :identifier OR u.phoneNumber = :identifier
          """)
    Optional<User> findByIdentifier(@Param("identifier") String identifier);


}
