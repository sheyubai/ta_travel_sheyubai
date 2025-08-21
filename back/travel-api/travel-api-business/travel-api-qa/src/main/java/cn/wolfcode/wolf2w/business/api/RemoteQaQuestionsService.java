package cn.wolfcode.wolf2w.business.api;

import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.business.api.domain.QaQuestions;
import cn.wolfcode.wolf2w.business.api.factory.RemoteQaQuestionsFallbackFactory;
import cn.wolfcode.wolf2w.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
* 问答提问 远程服务
 *
 * @author yang
 * @date 2025-07-24
 */
@FeignClient(contextId = "RemoteQaQuestionsService", name = ServiceNameConstants.QUESTION_SERVICE, fallbackFactory = RemoteQaQuestionsFallbackFactory.class)
public interface RemoteQaQuestionsService {

    @GetMapping("/qaQuestions/feign/list")
    R<List<QaQuestions>> list(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/qaQuestions/feign/{id}")
    R<QaQuestions> getOne(@PathVariable("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
