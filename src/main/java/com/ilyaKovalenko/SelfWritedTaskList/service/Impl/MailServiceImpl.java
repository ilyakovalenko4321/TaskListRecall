package com.ilyaKovalenko.SelfWritedTaskList.service.Impl;

import com.ilyaKovalenko.SelfWritedTaskList.domain.Mail.MailType;
import com.ilyaKovalenko.SelfWritedTaskList.domain.User.User;
import com.ilyaKovalenko.SelfWritedTaskList.service.MailService;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender mailSender;

    @Override
    @SneakyThrows
    public void sendEmail(User user, MailType type, Properties params, String secretKey) {
        switch(type){
            case REMINDER -> sendReminderEmail(user, params);
            case REGISTRATION -> sendRegistrationEmail(user, params, secretKey);
            default ->  {}
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(User user, Properties params, String secretKey) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setSubject("Thank you for registration" + user.getName());
        helper.setTo(user.getEmail());
        helper.setFrom("tasklisttestmail@gmail.com");
        String emailContent = getRegistrationEmailContent(user, params);

        emailContent += "<br><br>Your secret key is: <strong>" + secretKey + "</strong>";

        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void sendReminderEmail(User user, Properties params) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setSubject("You have task to do in 1 hour");
        helper.setTo(user.getEmail());
        helper.setFrom("tasklisttestmail@gmail.com");
        String emailContent = getReminderEmailContent(user, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(User user, Properties properties){
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getUsername());
        configuration.getTemplate("register.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getReminderEmailContent(User user, Properties properties){
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getUsername());
        model.put("title", properties.getProperty("task.title"));
        model.put("description", properties.getProperty("task.description"));
        configuration.getTemplate("reminder.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }


}
