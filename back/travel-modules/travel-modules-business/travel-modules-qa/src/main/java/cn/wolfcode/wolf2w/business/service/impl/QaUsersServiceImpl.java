package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.domain.QaUsers;
import cn.wolfcode.wolf2w.business.mapper.QaUsersMapper;
import cn.wolfcode.wolf2w.business.query.QaUsersQuery;
import cn.wolfcode.wolf2w.business.service.IQaUsersService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问答用户Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-24
 */
@Service
@Transactional
public class QaUsersServiceImpl extends ServiceImpl<QaUsersMapper,QaUsers> implements IQaUsersService {

    @Override
    public IPage<QaUsers> queryPage(QaUsersQuery qo) {
        IPage<QaUsers> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        return lambdaQuery()
                .page(page);
    }
}
