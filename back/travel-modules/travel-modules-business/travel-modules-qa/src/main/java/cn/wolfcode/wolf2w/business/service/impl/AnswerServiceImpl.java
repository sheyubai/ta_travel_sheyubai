package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.domain.Answer;
import cn.wolfcode.wolf2w.business.mapper.AnswerMapper;
import cn.wolfcode.wolf2w.business.query.AnswerQuery;
import cn.wolfcode.wolf2w.business.service.IAnswerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 问题回答Service业务层处理
 * 
 * @author yang
 * @date 2025-08-12
 */
@Service
@Transactional
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper,Answer> implements IAnswerService {

    @Override
    public IPage<Answer> queryPage(AnswerQuery qo) {
        IPage<Answer> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        return lambdaQuery()
                .page(page);
    }


    //更具问题id查评论
    @Override
    public List<Answer> getByQuestionId(Long id) {
        List<Answer> list = lambdaQuery().eq(Answer::getQuestionId, id).list();
        return list;
    }
}
