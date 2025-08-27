package cn.wolfcode.wolf2w.business.service;

import cn.wolfcode.wolf2w.business.api.domain.Answer;
import cn.wolfcode.wolf2w.business.query.AnswerQuery;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 问题回答Service接口
 * 
 * @author yang
 * @date 2025-08-12
 */
public interface IAnswerService extends IService<Answer>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<Answer> queryPage(AnswerQuery qo);

    List<Answer> getByQuestionId(Long id);

    void addAnswer(Answer answer);
}
