package com.project.we_go_jim.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaEmailSenderConfig {

    Dotenv dotenv = Dotenv.load();

    private final String MAIL_HOST = dotenv.get("MAIL_HOST");
    private final String MAIL_USERNAME = dotenv.get("MAIL_USERNAME");
    private final String MAIL_PASSWORD = dotenv.get("MAIL_PASSWORD");
    private final String MAIL_PORT = dotenv.get("MAIL_PORT");

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Set up Gmail config
        mailSender.setHost(MAIL_HOST);
        mailSender.setPort(Integer.parseInt(MAIL_PORT));

        // Set up email config (using udeesa email)
        mailSender.setUsername(MAIL_USERNAME);
        mailSender.setPassword(MAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "debug");

        return mailSender;
    }
}
