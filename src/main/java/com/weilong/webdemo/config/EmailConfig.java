package com.weilong.webdemo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String emailForm;

}
