package com.ilyaKovalenko.SelfWritedTaskList.service.props;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Component
@Data
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    private String host;
    private String username;
    private String password;
    private int port;
    private Properties properties;

}
