package com.weilong.webdemo.service.email;

import com.weilong.webdemo.config.EmailConfig;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private EmailConfig emailConfig;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void sendSimpleMail(String sendTo, String title, String content) {
        //简单邮件发送
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getEmailForm());
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentMail(String sendTo, String title, String content, File file) {
        MimeMessage msg = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg,true);
            helper.setFrom(emailConfig.getEmailForm());
            helper.setTo(sendTo);
            helper.setText(content);
            helper.setSubject(title);
            FileSystemResource r = new FileSystemResource(file);
            helper.addAttachment("附件",r);
        } catch (Exception e){
            e.printStackTrace();
        }
        mailSender.send(msg);
    }

    @Override
    public void sendTemplateMail(String sendTo, String title, String info) {
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg,true);
            helper.setFrom(emailConfig.getEmailForm());
            helper.setTo(sendTo);
            helper.setSubject(title);

            Map<String, Object> map = new HashMap<>();
            map.put("username","李白");

            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(info);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            helper.setText(html,true);
        } catch (Exception e){
            e.printStackTrace();
        }
        mailSender.send(msg);
    }
}
