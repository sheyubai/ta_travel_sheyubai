package cn.wolfcode.wolf2w.business.api;

import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.business.api.domain.StrategyTheme;
import cn.wolfcode.wolf2w.business.api.factory.RemoteStrategyThemeFallbackFactory;
import cn.wolfcode.wolf2w.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
* 攻略主题 远程服务
 *
 * @author klq
 * @date 2025-07-22
 */
@FeignClient(contextId = "RemoteStrategyThemeService", name = ServiceNameConstants.STRATEGY_SERVICE, fallbackFactory = RemoteStrategyThemeFallbackFactory.class)
public interface RemoteStrategyThemeService {

    @GetMapping("/strategyThemes/feign/list")
    R<List<StrategyTheme>> list(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/strategyThemes/feign/{id}")
    R<StrategyTheme> getOne(@PathVariable("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
