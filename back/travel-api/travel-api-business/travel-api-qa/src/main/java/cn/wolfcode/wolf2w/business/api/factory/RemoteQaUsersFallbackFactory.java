package cn.wolfcode.wolf2w.business.api.factory;

import cn.wolfcode.wolf2w.business.api.RemoteQaUsersService;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.business.api.domain.QaUsers;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问答用户 远程服务降级回调
 *
 * @author ruoyi
 * @date 2025-07-24
 */
@Component
public class RemoteQaUsersFallbackFactory implements FallbackFactory<RemoteQaUsersService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteQaUsersFallbackFactory.class);

    @Override
    public RemoteQaUsersService create(Throwable throwable) {
        log.error("问答用户服务调用失败:{}", throwable.getMessage());

        return new RemoteQaUsersService() {
            @Override
            public R<List<QaUsers>> list(String source) {
                return R.fail(Lists.newArrayList(), "查询问答用户列表信息失败:" + throwable.getMessage());
            }

            @Override
            public R<QaUsers> getOne(Long id, String source) {
                return R.fail("查询问答用户信息失败:" + throwable.getMessage());
            }
        };
    }
}
