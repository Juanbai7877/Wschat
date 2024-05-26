package org.websocketchat.websocketchat.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.websocketchat.websocketchat.utils.SendMailUtils;

/**
 * @author ALL
 * @date 2024/4/25
 * @Description
 */
@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    SendMailUtils sendMailUtils;

    @GetMapping("/sendMails")
    public String sendMails() {
        sendMailUtils.sendText("发送带页面格式加文件邮件测试", "发送带页面格式加文件邮件测试", "websocketchat@163.com",
                " 1787737349@qq.com");
        return "ok";
    }

}
