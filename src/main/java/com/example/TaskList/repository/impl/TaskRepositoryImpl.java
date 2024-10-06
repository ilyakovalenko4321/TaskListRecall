package com.example.TaskList.repository.impl;

import com.example.TaskList.domain.exception.ResourceMappingException;
import com.example.TaskList.domain.task.Task;
import com.example.TaskList.repository.TaskRepository;
import com.example.TaskList.repository.mappers.TaskRowMapper;
import com.example.TaskList.repository.DataSourceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;


//@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;

    @Override
    public Optional<Task> findById(Long id) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String FIND_BY_ID = """
                             SELECT t.id as task_id,
                                    t.title as task_title,
                                    t.description as task_description,
                                    t.expiration_date as task_expiration_date,
                                    t.status as task_status
                             from tasks t
                             WHERE id = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                return Optional.ofNullable(TaskRowMapper.mapRow(resultSet));
            }
        }catch (SQLException throwable){
            throw new ResourceMappingException("Error while finding user by id");
        }
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String FIND_ALL_BY_USER_ID = """
                    SELECT t.id as task_id,
                           t.title as task_title,
                           t.description as task_description,
                           t.expiration_date as task_expiration_date,
                           t.status as task_status
                    from tasks t
                    JOIN users_tasks ut on t.id = ut.task_id
                    WHERE ut.user_id = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            statement.setLong(1, userId);
            try(ResultSet resultSet = statement.executeQuery()){
                return TaskRowMapper.mapRows(resultSet);
            }
        }catch (SQLException throwable){
            throw new ResourceMappingException("Error while finding all user by id");
        }
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String ASSIGN = """
                    INSERT INTO users_tasks (task_id, user_id)
                    VALUES (?, ?)
                    """;
            PreparedStatement statement = connection.prepareStatement(ASSIGN);
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }catch (SQLException throwable){
            throw new ResourceMappingException("Error while assign task to user by id");
        }
    }

    @Override
    public void update(Task task) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String UPDATE = """
                    UPDATE tasks
                    SET title=?,
                        description=?,
                        expiration_date=?,
                        status=?
                    WHERE id=?
                    """;
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, task.getTitle());
            if (task.getDescription()!=null){
                statement.setString(2, task.getDescription());
            }else{
                statement.setNull(2, Types.VARCHAR);
            }
            if (task.getExpirationDate()!=null) {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }else{
                statement.setNull(3,Types.TIMESTAMP);
            }
            statement.setString(4, task.getStatus().name());
            statement.setLong(5, task.getId());
            statement.executeUpdate();
        }catch (SQLException throwable){
            throw new ResourceMappingException("Error while updating task");
        }

    }

    @Override
    public void create(Task task) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String CREATE = """
                    INSERT INTO tasks(title, description, expiration_date, status)
                    VALUES (?,?,?,?)
                    """;
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, task.getTitle());
            if (task.getDescription()!=null){
                statement.setString(2, task.getDescription());
            }else{
                statement.setNull(2, Types.VARCHAR);
            }
            if (task.getExpirationDate()!=null) {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }else{
                statement.setNull(3,Types.TIMESTAMP);
            }
            statement.setString(4, task.getStatus().name());
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()){
                resultSet.next();
                task.setId(resultSet.getLong(1));
            }
        }catch (SQLException throwable){
            throw new ResourceMappingException("Error while creating task");
        }
    }

    @Override
    public void delete(Long id) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            String DELETE = """
                    DELETE FROM tasks
                    WHERE id = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(DELETE);
           statement.setLong(1, id);
           statement.executeUpdate();
        }catch (SQLException throwable){
            throw new ResourceMappingException("Error while deleting task");
        }


    }
}
