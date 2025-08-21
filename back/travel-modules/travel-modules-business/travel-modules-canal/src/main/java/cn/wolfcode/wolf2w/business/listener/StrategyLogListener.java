package cn.wolfcode.wolf2w.business.listener;

import cn.wolfcode.wolf2w.business.domain.StrategyCanal;
import cn.wolfcode.wolf2w.common.rabbit.config.TravelRabiitConfig;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@Component
@CanalTable("ta_strategy")
public class StrategyLogListener implements EntryHandler<StrategyCanal> {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void insert(StrategyCanal strategyCanal) {
        System.out.println("新增"+strategyCanal);
        //同步mysql信息到查询库
        String message = JSON.toJSONString(strategyCanal);
        amqpTemplate.convertAndSend(TravelRabiitConfig.StrategyExchangeName, TravelRabiitConfig.StrategyKey, message);
        //todo 同步信息到缓存库

        //todo 同步信息到日志库

    }


}
