package cn.wolfcode.wolf2w.business.api;

import cn.wolfcode.wolf2w.business.api.domain.Answer;
import cn.wolfcode.wolf2w.business.api.factory.RemoteAnswerFallbackFactory;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
* 问题回答 远程服务
 *
 * @author yang
 * @date 2025-08-12
 */
@FeignClient(contextId = "RemoteAnswerService", name = ServiceNameConstants.QUESTION_SERVICE, fallbackFactory = RemoteAnswerFallbackFactory.class)
public interface RemoteAnswerService {

    @GetMapping("/answers/feign/list")
    R<List<Answer>> list(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/answers/feign/{id}")
    R<Answer> getOne(@PathVariable("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
