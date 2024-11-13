package com.ilyaKovalenko.SelfWritedTaskList.service;

import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User create(User user);

    User update(User user);

    boolean isTaskOwner(Long userId, Long taskId);

    void delete(Long id);


}
