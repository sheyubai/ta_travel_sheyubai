package cn.wolfcode.wolf2w.job.task;

import cn.wolfcode.wolf2w.business.api.RemoteStrategyService;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("strategyTask")
public class StrategyTask {

    @Autowired
    private RemoteStrategyService remoteStrategyService;

    public void statisRank() {
        System.out.println("排行榜统计任务");
        remoteStrategyService.statisRank(SecurityConstants.INNER);
    }

    public void statisCondition() {
        System.out.println("1");
        remoteStrategyService.statisCondition(SecurityConstants.INNER);
    }

    public void statisHashPersistence() {
        System.out.println("统计任务");
        remoteStrategyService.statisHashPersistence("inner");
    }
}
//strategyTask.statisHashPersistence
