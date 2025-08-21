package cn.wolfcode.wolf2w.business.api.factory;

import cn.wolfcode.wolf2w.business.api.RemoteAnswerService;
import cn.wolfcode.wolf2w.business.api.domain.Answer;

import cn.wolfcode.wolf2w.common.core.domain.R;

import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问题回答 远程服务降级回调
 *
 * @author yang
 * @date 2025-08-12
 */
@Component
public class RemoteAnswerFallbackFactory implements FallbackFactory<RemoteAnswerService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteAnswerFallbackFactory.class);

    @Override
    public RemoteAnswerService create(Throwable throwable) {
        log.error("问题回答服务调用失败:{}", throwable.getMessage());

        return new RemoteAnswerService() {
            @Override
            public R<List<Answer>> list(String source) {
                return R.fail(Lists.newArrayList(), "查询问题回答列表信息失败:" + throwable.getMessage());
            }

            @Override
            public R<Answer> getOne(Long id, String source) {
                return R.fail("查询问题回答信息失败:" + throwable.getMessage());
            }
        };
    }
}
