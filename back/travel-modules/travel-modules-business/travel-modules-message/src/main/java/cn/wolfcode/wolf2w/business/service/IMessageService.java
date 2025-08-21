package cn.wolfcode.wolf2w.business.service;

import org.springframework.stereotype.Service;


public interface IMessageService {
    void sendVerifyCode(String phone);

}
