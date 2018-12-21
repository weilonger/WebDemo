package com.weilong.webdemo.controller;

import com.weilong.webdemo.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Random;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping("/sendMail")
    @ResponseBody
    public String sendSimpleMail(){
        Random random = new Random();
        String sendTo = "1448245883@qq.com";
        String title = getClass() + "测试邮件发送" + random.nextInt(100);
        String content = "这是一封邮件发送测试，请收到后不要删除，发qq给1448245883以验证";
        emailService.sendSimpleMail(sendTo, title, content);
        return "success";
    }

    @RequestMapping("sendText")
    @ResponseBody
    public String sendAttachmentMail(){
        File file = new File("E:/工作/bookmarks_2018_9_28.html");
        Random random = new Random();
        String sendTo = "1448245883@qq.com";
        String title = getClass() + "测试附件发送" + random.nextInt(100);
        String content = "这是一封邮件发送测试";
        emailService.sendAttachmentMail(sendTo, title, content, file);
        return "success";
    }

    @RequestMapping("/sendHtml")
    @ResponseBody
    public String sendTemplateMail(){
        Random random = new Random();
        String sendTo = "1448245883@qq.com";
        String title = getClass() + "测试附件发送" + random.nextInt(100);
        String html = "info.html";
        emailService.sendTemplateMail(sendTo,title,html);
        return "success";
    }
}
