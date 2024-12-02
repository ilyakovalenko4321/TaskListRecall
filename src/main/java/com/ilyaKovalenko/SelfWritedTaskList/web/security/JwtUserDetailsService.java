package com.ilyaKovalenko.SelfWritedTaskList.web.security;


import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.AccessDeniedException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.ResourceNotFoundException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.Role;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtEntity loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = new User();
        if (identifier.matches("^\\+?[0-9]{10,15}$")) {
            // Если идентификатор соответствует номеру телефона
            user = userService.getByPhoneNumber(identifier);
        } else if (identifier.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            // Если идентификатор соответствует email
            user = userService.getByEmail(identifier);
        } else if (identifier.matches("^[a-zA-Z0-9._-]{3,}$")) {
            // Если идентификатор соответствует username
            user = userService.getByUsername(identifier);
        } else {
            throw new ResourceNotFoundException("Invalid identifier format");
        }

        if(user.getRoles().contains(Role.ROLE_BLOCKED)){
            throw new AccessDeniedException("You should confirm your email");
        }

        return JwtEntityFactory.create(user);
    }
}