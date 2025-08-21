package cn.wolfcode.wolf2w.business.api;

import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.business.api.domain.QaUsers;
import cn.wolfcode.wolf2w.business.api.factory.RemoteQaUsersFallbackFactory;
import cn.wolfcode.wolf2w.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
* 问答用户 远程服务
 *
 * @author ruoyi
 * @date 2025-07-24
 */
@FeignClient(contextId = "RemoteQaUsersService", name = ServiceNameConstants.DESTINATION_SERVICE, fallbackFactory = RemoteQaUsersFallbackFactory.class)
public interface RemoteQaUsersService {

    @GetMapping("/qaUserss/feign/list")
    R<List<QaUsers>> list(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/qaUserss/feign/{id}")
    R<QaUsers> getOne(@PathVariable("id") Long id, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
