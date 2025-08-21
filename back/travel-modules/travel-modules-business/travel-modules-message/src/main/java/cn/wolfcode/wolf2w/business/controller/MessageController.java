package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.business.service.IMessageService;
import cn.wolfcode.wolf2w.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sms")
@RestController
public class MessageController {
    @Autowired
    private IMessageService messageService;
    @PostMapping("/sendVerifyCode")
    public Object sendVerifyCode(String phone) {
        messageService.sendVerifyCode(phone);
        return R.ok();
    }
}
