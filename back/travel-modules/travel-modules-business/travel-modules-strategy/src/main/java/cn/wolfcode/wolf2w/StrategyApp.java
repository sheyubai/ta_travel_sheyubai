package cn.wolfcode.wolf2w;

import cn.wolfcode.wolf2w.business.api.domain.StrategyContent;
import cn.wolfcode.wolf2w.common.security.annotation.EnableCustomConfig;
import cn.wolfcode.wolf2w.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRyFeignClients
@EnableCustomConfig
public class StrategyApp {
    public static void main(String[] args) {
        SpringApplication.run(StrategyApp.class, args);
    }
}
