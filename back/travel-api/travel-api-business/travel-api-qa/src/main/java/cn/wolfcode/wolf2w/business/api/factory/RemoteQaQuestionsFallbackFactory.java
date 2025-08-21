package cn.wolfcode.wolf2w.business.api.factory;

import cn.wolfcode.wolf2w.business.api.RemoteQaQuestionsService;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.business.api.domain.QaQuestions;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问答提问 远程服务降级回调
 *
 * @author yang
 * @date 2025-07-24
 */
@Component
public class RemoteQaQuestionsFallbackFactory implements FallbackFactory<RemoteQaQuestionsService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteQaQuestionsFallbackFactory.class);

    @Override
    public RemoteQaQuestionsService create(Throwable throwable) {
        log.error("问答提问服务调用失败:{}", throwable.getMessage());

        return new RemoteQaQuestionsService() {
            @Override
            public R<List<QaQuestions>> list(String source) {
                return R.fail(Lists.newArrayList(), "查询问答提问列表信息失败:" + throwable.getMessage());
            }

            @Override
            public R<QaQuestions> getOne(Long id, String source) {
                return R.fail("查询问答提问信息失败:" + throwable.getMessage());
            }
        };
    }
}
