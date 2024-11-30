package com.ilyaKovalenko.SelfWritedTaskList.config;

import com.ilyaKovalenko.SelfWritedTaskList.repository.TaskRepository;
import com.ilyaKovalenko.SelfWritedTaskList.repository.UserRepository;
import com.ilyaKovalenko.SelfWritedTaskList.service.ImageService;
import com.ilyaKovalenko.SelfWritedTaskList.service.Impl.AuthServiceImpl;
import com.ilyaKovalenko.SelfWritedTaskList.service.Impl.ImageServiceImpl;
import com.ilyaKovalenko.SelfWritedTaskList.service.Impl.TaskServiceImpl;
import com.ilyaKovalenko.SelfWritedTaskList.service.Impl.UserServiceImpl;
import com.ilyaKovalenko.SelfWritedTaskList.service.MailService;
import com.ilyaKovalenko.SelfWritedTaskList.service.props.JwtProperties;
import com.ilyaKovalenko.SelfWritedTaskList.service.props.MinioProperties;
import com.ilyaKovalenko.SelfWritedTaskList.web.security.JwtTokenProvider;
import com.ilyaKovalenko.SelfWritedTaskList.web.security.JwtUserDetailsService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    @Bean
    @Primary
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(
                "dmdqYmhqbmttYmNhamNjZWhxa25hd2puY2xhZWtic3ZlaGtzYmJ1dg=="
        );
        return jwtProperties;
    }

    @Bean
    public UserDetailsService userDetailsService(
            final UserRepository userRepository
    ) {
        return new JwtUserDetailsService(userService(userRepository));
    }

    @Bean
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        MinioProperties properties = new MinioProperties();
        properties.setBucket("images");
        return properties;
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new ImageServiceImpl(minioClient(), minioProperties());
    }

    @Bean
    public JwtTokenProvider tokenProvider(
            final UserRepository userRepository
    ) {
        return new JwtTokenProvider(jwtProperties(),
                userDetailsService(userRepository),
                userService(userRepository));
    }

    @Bean
    @Primary
    public UserServiceImpl userService(
            final UserRepository userRepository
    ) {
        return new UserServiceImpl(
                userRepository,
                testPasswordEncoder(),
        );
    }

    @Bean
    @Primary
    public TaskServiceImpl taskService(
            final TaskRepository taskRepository,
            final ImageService imageService
    ) {
        return new TaskServiceImpl(taskRepository, imageService);
    }


    @Bean
    @Primary
    public AuthServiceImpl authService(
            final UserRepository userRepository,
            final AuthenticationManager authenticationManager
    ) {
        return new AuthServiceImpl(
                authenticationManager,
                userService(userRepository),
                tokenProvider(userRepository)
        );
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }

}
