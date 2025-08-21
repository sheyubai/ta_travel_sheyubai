package cn.wolfcode.wolf2w.business.listener;

import cn.wolfcode.wolf2w.business.api.domain.Strategy;
import cn.wolfcode.wolf2w.business.api.domain.StrategyEs;
import cn.wolfcode.wolf2w.business.repository.StrategyEsRepository;
import cn.wolfcode.wolf2w.common.rabbit.config.TravelRabiitConfig;
import com.alibaba.fastjson.JSON;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.json.Json;


@Component
public class MyRabbitListener {

    @Autowired
    private StrategyEsRepository strategyEsRepository;

    @RabbitListener(queues = {TravelRabiitConfig.StrategyQueueName})
    public void onmessage(String message){
        Strategy strategy = JSON.parseObject(message, Strategy.class);
        StrategyEs es=new StrategyEs();
        BeanUtils.copyProperties(strategy,es);
        strategyEsRepository.save(es);
    }
}
