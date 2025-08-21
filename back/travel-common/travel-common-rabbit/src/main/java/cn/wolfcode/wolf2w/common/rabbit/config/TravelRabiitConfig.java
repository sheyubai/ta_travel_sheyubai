package cn.wolfcode.wolf2w.common.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelRabiitConfig {

    public static final String StrategyQueueName = "StrategyRabbit";
    public static final String StrategyExchangeName = "StrategyRabbitExchange";
    public static final String StrategyKey = "StrategyKey";

    @Bean
    public Queue StrategyQueue() {
        return new Queue(StrategyQueueName, true, false, false);
    }

    @Bean
    public DirectExchange StrategyExchange() {
        return new DirectExchange(StrategyExchangeName, true, false);
    }


    @Bean
    public Binding StrategyBinding() {
        return new Binding(StrategyQueueName, Binding.DestinationType.QUEUE, StrategyExchangeName, StrategyKey, null);
    }

}
