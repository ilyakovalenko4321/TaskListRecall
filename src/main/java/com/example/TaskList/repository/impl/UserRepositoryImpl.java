package com.example.TaskList.repository.impl;

import com.example.TaskList.domain.exception.ResourceMappingException;
import com.example.TaskList.domain.exception.ResourceNotFoundException;
import com.example.TaskList.domain.user.Role;
import com.example.TaskList.domain.user.User;
import com.example.TaskList.repository.UserRepository;
import com.example.TaskList.repository.DataSourceConfig;
import com.example.TaskList.repository.mappers.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            String FIND_BY_ID = """
                        SELECT u.id as user_id,
                               u.name as user_name,
                               u.username as user_username,
                               u.password as user_password,
                               ur.role as user_role_role,
                               t.id as task_id,
                               t.title as task_title,
                               t.description as task_description,
                               t.expiration_date as task_expiration_date,
                               t.status as task_status
                        from users u
                            LEFT JOIN users_roles ur on u.id = ur.user_id
                            LEFT JOIN users_tasks ut on u.id = ut.user_id
                            LEFT JOIN tasks t on ut.task_id = t.id
                        WHERE u.id=?
                    """;
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        }
        catch(SQLException throwables){
            throw new ResourceNotFoundException("Error while finding user by id");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();

            String FIND_BY_USERNAME = """
                    SELECT u.id as user_id,
                           u.name as user_name,
                           u.username as user_username,
                           u.password as user_password,
                           ur.role as user_role_role,
                           t.id as task_id,
                           t.title as task_title,
                           t.description as task_description,
                           t.expiration_date as task_expiration_date,
                           t.status as task_status
                    from users u
                        LEFT JOIN users_roles ur on u.id = ur.user_id
                        LEFT JOIN users_tasks ut on u.id = ut.user_id
                        LEFT JOIN tasks t on ut.task_id = t.id
                    WHERE u.username=?
                    """;
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, username);
            try(ResultSet resultSet = statement.executeQuery()){
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        }
        catch(SQLException throwables){
            throw new ResourceNotFoundException("Error while finding user by username");
        }
    }

    @Override
    public void update(User user) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String UPDATE = """
                    UPDATE users
                        SET name=?,
                        username=?,
                        password=?
                    WHERE id = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());

        }catch(SQLException throwables){
            throw new ResourceMappingException("Error while updating user");
        }
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user The user object to be inserted.
     * @throws ResourceMappingException if an error occurs while creating the user.
     */
    @Override
    public void create(User user) {
        try (Connection connection = dataSourceConfig.getConnection()) {
            String CREATE = """
                    INSERT INTO users(name, username, password)
                    VALUES(?,?,?)
                    """;
            try (PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getName());
                statement.setString(2, user.getUsername());
                statement.setString(3, user.getPassword());
                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating user: " + e.getMessage());
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String INSERT_USER_ROLE = """
                    INSERT INTO users_roles(user_id, role)
                    VALUES(?,?)
                    """;
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setString(2, role.name());
        }catch(SQLException throwables){
            throw new ResourceMappingException("Error while insert role to user");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String IS_TASK_OWNER = """
                    SELECT EXISTS(
                        SELECT 1
                        FROM users_tasks
                        WHERE user_id = ?
                          AND task_id = ?
                    );
                    """;
            PreparedStatement statement = connection.prepareStatement(IS_TASK_OWNER);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            statement.executeQuery();
            try(ResultSet resultSet = statement.executeQuery()){
                resultSet.next();
                return resultSet.getBoolean(1);
            }
        }catch(SQLException throwables){
            throw new ResourceMappingException("EXCEPTION while checking if user is task owner");
        }
    }

    @Override
    public void delete(Long id) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String DELETE = """
                    DELETE FROM users
                    where id = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("EXCEPTION while deleting user");
        }
    }
}
