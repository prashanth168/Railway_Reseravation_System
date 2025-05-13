package com.railway.config;
import java.util.Properties;

public class EmailConfig {
    public static Properties getEmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", AppConfig.getProperty("email.host"));
        props.put("mail.smtp.port", AppConfig.getProperty("email.port"));
        props.put("mail.smtp.auth", AppConfig.getProperty("email.auth"));
        props.put("mail.smtp.starttls.enable", AppConfig.getProperty("email.starttls.enable"));
        return props;
    }

    public static String getEmailUsername() {
        return AppConfig.getProperty("email.username");
    }

    public static String getEmailPassword() {
        return AppConfig.getProperty("email.password");
    }

    public static String getEmailFrom() {
        return AppConfig.getProperty("email.from");
    }
}