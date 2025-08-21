package cn.wolfcode.wolf2w.business.service;

import cn.wolfcode.wolf2w.business.api.domain.QaQuestions;
import cn.wolfcode.wolf2w.business.query.QaQuestionsQuery;
import cn.wolfcode.wolf2w.business.vo.QuestionAskVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 问答提问Service接口
 * 
 * @author yang
 * @date 2025-07-24
 */
public interface IQaQuestionsService extends IService<QaQuestions>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<QaQuestions> queryPage(QaQuestionsQuery qo);

    void addAsk(QuestionAskVo questionAskVo);

    QaQuestions getdetail(Long id);
}
