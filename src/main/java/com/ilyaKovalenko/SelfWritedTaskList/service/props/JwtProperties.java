package com.ilyaKovalenko.SelfWritedTaskList.service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("security.jwt")
public class JwtProperties {

    private String secret;
    private Integer access;
    private Long refresh;

}
