package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.service.IMessageService;
import cn.wolfcode.wolf2w.business.util.SmsUtil;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Key;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private RedisService redisService;

    @Override
    public void sendVerifyCode(String phone) {
        Random random = new Random();
        Integer rand=random.nextInt(900000)+100000;
        System.err.println("验证吗是："+rand);
        SmsUtil.senSmsAli(phone, rand.toString());
        //存储验证码到redis
        String key = RedisKeys.VERIFY_CODE.join(phone);
        long time = RedisKeys.VERIFY_CODE.getTime();
        String value = rand.toString();

        redisService.setCacheObject(key,value,time, TimeUnit.SECONDS);
    }
}
