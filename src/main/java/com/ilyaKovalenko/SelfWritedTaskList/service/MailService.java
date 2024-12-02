package com.ilyaKovalenko.SelfWritedTaskList.service;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Mail.MailType;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(User user, MailType type, Properties params, String secretKey);

}
