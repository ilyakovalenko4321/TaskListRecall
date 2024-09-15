package com.example.TaskList.web.mappers;


import com.example.TaskList.domain.user.User;
import com.example.TaskList.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
    User toEntity(UserDto dto);

}
