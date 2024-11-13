package com.ilyaKovalenko.SelfWritedTaskList.web.security.expression;


import com.ilyaKovalenko.SelfWritedTaskList.domain.User.Role;
import com.ilyaKovalenko.SelfWritedTaskList.service.UserService;
import com.ilyaKovalenko.SelfWritedTaskList.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public boolean canAccessUser(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtEntity user = (JwtEntity) authentication.getPrincipal();

        Long userId = user.getId();

        return id.equals(userId) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean hasAnyRole(Authentication authentication, Role ... roles){
        for (Role role: roles){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if(authentication.getAuthorities().contains(authority)) return true;
        }
        return false;
    }

    public boolean canAccessTask( Long taskId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtEntity user = (JwtEntity) authentication.getPrincipal();

        return userService.isTaskOwner(user.getId(), taskId) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

}
