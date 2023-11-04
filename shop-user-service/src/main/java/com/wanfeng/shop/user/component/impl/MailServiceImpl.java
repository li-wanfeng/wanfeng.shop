package com.wanfeng.shop.user.component.impl;

import com.wanfeng.shop.user.component.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value(value = "${spring.mail.from}")
    private String from;
    @Override
    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //设置发送人
//        simpleMailMessage.setFrom(from);
//        simpleMailMessage.setTo(to);
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage.setText(content);
//        mailSender.send(simpleMailMessage);
        log.info("邮件发送成功:{}",simpleMailMessage.toString());
    }
}
