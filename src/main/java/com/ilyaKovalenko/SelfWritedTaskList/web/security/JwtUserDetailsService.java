package com.ilyaKovalenko.SelfWritedTaskList.web.security;


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

    public JwtEntity loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userService.getByUsername(username);
        return JwtEntityFactory.create(user);
    }
}
