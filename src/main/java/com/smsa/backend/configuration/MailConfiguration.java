package com.smsa.backend.configuration;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.security.GeneralSecurityException;
import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Bean
    public JavaMailSender javaMailSender() throws GeneralSecurityException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.smsaexpress.com");
        mailSender.setPort(587);
        mailSender.setUsername("cdvbill@smsaexpress.com");
        mailSender.setPassword("Stb@1231231");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Insecure: trust all SSL certificates
        MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
        socketFactory.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.socketFactory", socketFactory);
        props.put("mail.smtp.ssl.checkserveridentity", "false");

        return mailSender;
    }
}
