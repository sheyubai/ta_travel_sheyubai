package cn.wolfcode.wolf2w.business;

import cn.wolfcode.wolf2w.common.security.annotation.EnableCustomConfig;
import cn.wolfcode.wolf2w.common.security.annotation.EnableRyFeignClients;
import cn.wolfcode.wolf2w.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCustomSwagger2
@SpringBootApplication
@EnableRyFeignClients
@EnableCustomConfig
public class DestinationApp {
    public static void main(String[] args) {
        SpringApplication.run(DestinationApp.class, args);
    }
}
