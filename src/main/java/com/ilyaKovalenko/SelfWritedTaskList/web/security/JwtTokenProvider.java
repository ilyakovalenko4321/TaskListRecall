package com.ilyaKovalenko.SelfWritedTaskList.web.security;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Exception.AccessDeniedException;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.Role;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.service.UserService;
import com.ilyaKovalenko.SelfWritedTaskList.service.props.JwtProperties;
import com.ilyaKovalenko.SelfWritedTaskList.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init(){this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());}

    public String createAccessToken(Long userId, String username, Set<Role> roles){
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .add("role", resolveRoles(roles))
                .build();

        Instant validity = Instant.now().plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    private List<String> resolveRoles(Set<Role> roles){
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public String createRefreshToken(Long id, String username){
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", id)
                .build();

        Instant validity = Instant.now().plus(jwtProperties.getRefresh(), ChronoUnit.HOURS);

        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserToken(String refreshToken){

        JwtResponse jwtResponse = new JwtResponse();
        if(!validateToken(refreshToken)){
            throw new AccessDeniedException("Refresh token is not valid");
        }
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));
        return jwtResponse;

    }

    public boolean validateToken(String token){
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);

        return claims.getPayload().getExpiration().after(new Date());
    }

    private Integer getId(String token){
        return Jwts
                .parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Integer.class);
    }

    private String getUsername(String token){
        return Jwts
                .parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Authentication getAuthentication(String token){
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
