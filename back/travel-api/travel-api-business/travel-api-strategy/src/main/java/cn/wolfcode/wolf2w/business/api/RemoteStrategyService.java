package cn.wolfcode.wolf2w.business.api;

import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.business.api.domain.Strategy;
import cn.wolfcode.wolf2w.business.api.factory.RemoteStrategyFallbackFactory;
import cn.wolfcode.wolf2w.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
* 攻略 远程服务
 *
 * @author klq
 * @date 2025-07-22
 */
@FeignClient(contextId = "RemoteStrategyService", name = ServiceNameConstants.STRATEGY_SERVICE, fallbackFactory = RemoteStrategyFallbackFactory.class)
public interface RemoteStrategyService {

    @GetMapping("/strategies/feign/list")
    R<List<Strategy>> list(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/strategies/feign/{id}")
    R<Strategy> getOne(@PathVariable("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @PostMapping("/strategies/feign/statisRank")
    R<?> statisRank(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/strategies/feign/statisCondition")
    R<?> statisCondition(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/strategies/feign/statisHashPersistence")
    R<?> statisHashPersistence(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
