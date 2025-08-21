package cn.wolfcode.wolf2w.business.service;

import cn.wolfcode.wolf2w.business.api.domain.QaUsers;
import cn.wolfcode.wolf2w.business.query.QaUsersQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 问答用户Service接口
 * 
 * @author ruoyi
 * @date 2025-07-24
 */
public interface IQaUsersService extends IService<QaUsers>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<QaUsers> queryPage(QaUsersQuery qo);
}
