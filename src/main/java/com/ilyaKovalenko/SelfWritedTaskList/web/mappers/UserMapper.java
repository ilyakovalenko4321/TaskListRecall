package com.ilyaKovalenko.SelfWritedTaskList.web.mappers;


import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.user.UserDto;

public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);

}
