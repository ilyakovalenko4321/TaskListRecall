package com.ilyaKovalenko.SelfWritedTaskList.config;


import com.ilyaKovalenko.SelfWritedTaskList.service.props.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setJavaMailProperties(mailProperties.getProperties());
        mailSender.getJavaMailProperties();
        return mailSender;
    }

}
