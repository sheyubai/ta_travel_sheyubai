package cn.wolfcode.wolf2w.business.api.factory;

import cn.wolfcode.wolf2w.business.api.RemoteStrategyThemeService;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.business.api.domain.StrategyTheme;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 攻略主题 远程服务降级回调
 *
 * @author klq
 * @date 2025-07-22
 */
@Component
public class RemoteStrategyThemeFallbackFactory implements FallbackFactory<RemoteStrategyThemeService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteStrategyThemeFallbackFactory.class);

    @Override
    public RemoteStrategyThemeService create(Throwable throwable) {
        log.error("攻略主题服务调用失败:{}", throwable.getMessage());

        return new RemoteStrategyThemeService() {
            @Override
            public R<List<StrategyTheme>> list(String source) {
                return R.fail(Lists.newArrayList(), "查询攻略主题列表信息失败:" + throwable.getMessage());
            }

            @Override
            public R<StrategyTheme> getOne(Long id, String source) {
                return R.fail("查询攻略主题信息失败:" + throwable.getMessage());
            }
        };
    }
}
