package com.wanfeng.shop.user.component;

public interface MailService {
    void sendMail(String to, String subject, String content);
}
