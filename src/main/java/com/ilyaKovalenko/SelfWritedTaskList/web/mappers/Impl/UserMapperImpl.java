package com.ilyaKovalenko.SelfWritedTaskList.web.mappers.Impl;

import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.user.UserDto;
import com.ilyaKovalenko.SelfWritedTaskList.web.mappers.UserMapper;
import org.springframework.stereotype.Component;

@Component("userMapperImpl")
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setName( user.getName() );
        userDto.setUsername( user.getUsername() );
        userDto.setPassword( user.getPassword() );
        userDto.setPasswordConfirmation( user.getPasswordConfirmation() );
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());

        return userDto;
    }

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId(dto.getId());
        user.setName( dto.getName() );
        user.setUsername( dto.getUsername() );
        user.setPassword( dto.getPassword() );
        user.setPasswordConfirmation( dto.getPasswordConfirmation() );
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }
}
