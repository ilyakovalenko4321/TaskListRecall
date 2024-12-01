package com.ilyaKovalenko.SelfWritedTaskList.repository;

import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
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


    @Query(value = """
            SELECT u.*
            FROM tasks t
            JOIN users_tasks u_t ON t.id = u_t.task_id
            JOIN users u ON u.id = u_t.user_id
            WHERE t.id = :taskId
            """, nativeQuery = true)
    Optional<User> findUserByTaskId(@Param("taskId") Long taskId);


    @Modifying
    @Query(value = """
            INSERT INTO unconfirmed_users (user_id, expiration_data, registration_attempt, access_key)
            VALUES (:user_id, :end, 0, :access_key)
            
            """, nativeQuery = true)
    void setConfirmation(@Param("user_id") Long user_id, @Param("end") Timestamp end, @Param("access_key") String accessKey);

    @Modifying
    @Query(value = """
    DELETE FROM users
    WHERE id IN (
        SELECT u_u.user_id
        FROM unconfirmed_users u_u
        WHERE u_u.expiration_data > :end
    )
    """, nativeQuery = true)
    void deleteAllSoonUnconfirmedUser(@Param("end") Timestamp end);

    @Query(value = """
            SELECT u_u.access_key
            FROM users u
            JOIN unconfirmed_users u_u ON u.id = u_u.user_id
            WHERE u.id = :id""", nativeQuery = true)
    Optional<String> findAccessKeyByUserId(@Param("id") Long id);

    @Modifying
    @Query(value = """
            UPDATE users_roles
            SET role = 'ROLE_USER'
            WHERE user_id = :id AND role = 'ROLE_BLOCKED'
            """, nativeQuery = true)
    void setUserRole(Long id);

    @Modifying
    @Query(value = """
            DELETE
            FROM unconfirmed_users
            WHERE user_id = :id
            """, nativeQuery = true)
    void deleteActivateUserFromUncomfired(@Param("id") Long id);


    @Modifying
    @Query(value = """
        DELETE FROM tasks
        WHERE id IN (
            SELECT t.id
            FROM tasks t
            JOIN users_tasks ut ON ut.task_id = t.id
            JOIN users u ON ut.user_id = u.id
            WHERE u.id = :id
        )
        """, nativeQuery = true)
    void deleteWiredTasks(@Param("id") Long id);


    @Query(value = "SELECT registration_attempt FROM unconfirmed_users WHERE user_id = :userId", nativeQuery = true)
    Integer findRemainingAttempts(@Param("userId") Long userId);

    // Нативный SQL-запрос для обновления количества попыток подтверждения
    @Modifying
    @Query(value = "UPDATE unconfirmed_users SET registration_attempt = :attempts WHERE user_id = :userId", nativeQuery = true)
    void changeAttemptsNumber(@Param("userId") Long userId, @Param("attempts") int attempts);

}
