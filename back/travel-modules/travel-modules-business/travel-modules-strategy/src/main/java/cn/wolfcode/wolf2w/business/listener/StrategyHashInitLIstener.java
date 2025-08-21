package cn.wolfcode.wolf2w.business.listener;

import cn.wolfcode.wolf2w.business.api.domain.Strategy;
import cn.wolfcode.wolf2w.business.service.IStrategyService;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StrategyHashInitLIstener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private IStrategyService strategyService;
    @Autowired
    private RedisService redisService;


    /*
     * 当时间到来的执行方法
     * 参数:事件对象
     * */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //预热数据（近期的热门数据）
        List<Strategy> list = strategyService.list();
        for (Strategy strategy : list) {
            String key = RedisKeys.STRATEGY_STATIS_HASH.join(strategy.getId().toString());
            if(redisService.hasKey(key)){
                continue;
            }
            Map map = new HashMap();
            map.put("strategyId", strategy.getId());
            map.put("viewnum", Integer.valueOf(strategy.getViewnum().toString()));
            map.put("replynum", Integer.valueOf(strategy.getReplynum().toString()));
            map.put("favornum", Integer.valueOf(strategy.getFavornum().toString()));
            map.put("sharenum", Integer.valueOf(strategy.getSharenum().toString()));
            map.put("thumbsupnum", Integer.valueOf(strategy.getThumbsupnum().toString()));
            redisService.setCacheMap(key, map);
        }
        System.out.println("攻略统计hash数据初始化完成");
    }

}
