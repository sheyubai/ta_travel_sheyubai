package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.RemoteDestinationService;
import cn.wolfcode.wolf2w.business.api.domain.Answer;
import cn.wolfcode.wolf2w.business.api.domain.Destination;
import cn.wolfcode.wolf2w.business.api.domain.QaQuestions;
import cn.wolfcode.wolf2w.business.mapper.QaQuestionsMapper;
import cn.wolfcode.wolf2w.business.query.QaQuestionsQuery;
import cn.wolfcode.wolf2w.business.service.IAnswerService;
import cn.wolfcode.wolf2w.business.service.IQaQuestionsService;
import cn.wolfcode.wolf2w.business.vo.QuestionAskVo;
import cn.wolfcode.wolf2w.business.vo.QuestionListVO;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.core.exception.ServiceException;
import cn.wolfcode.wolf2w.common.security.utils.SecurityUtils;
import cn.wolfcode.wolf2w.member.api.RemoteUserInfoService;
import cn.wolfcode.wolf2w.member.api.domain.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 问答提问Service业务层处理
 *
 * @author yang
 * @date 2025-07-24
 */
@Service
@Transactional
public class QaQuestionsServiceImpl extends ServiceImpl<QaQuestionsMapper, QaQuestions> implements IQaQuestionsService {

    @Autowired
    private RemoteUserInfoService remoteUserInfoService;
    @Autowired
    private RemoteDestinationService remoteDestinationService;
    @Autowired
    private IAnswerService answerService;

    @Override
    public IPage<QaQuestions> queryPage(QaQuestionsQuery qo) {
        IPage<QaQuestions> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        LambdaQueryWrapper<QaQuestions> wrapper = new LambdaQueryWrapper<>();

        if (qo.getTab() == 0) {
            wrapper.orderByDesc(QaQuestions::getCommentCount);
        } else if (qo.getTab() == 1) {
            wrapper.clear();
            wrapper.orderByDesc(QaQuestions::getCreateTime);
        } else if (qo.getTab() == 2) {
            wrapper.eq(QaQuestions::getCommentCount, 0);
            wrapper.orderByDesc(QaQuestions::getCreateTime);
        }
        baseMapper.selectPage(page, wrapper);

        for (QaQuestions qaQuestions : page.getRecords()) {
            if (qaQuestions.getLabelList() != null) {
                qaQuestions.setAllLabelList(qaQuestions.getLabelList().split(","));
                UserInfo users = remoteUserInfoService.getOne(qaQuestions.getAuthorId(), SecurityConstants.INNER).getData();
                qaQuestions.setUser(users);
            } else {
                qaQuestions.setAllLabelList(new String[0]);
            }
        }
        return page;
    }

    @Override
    public void addAsk(QuestionAskVo questionAskVo) {
        if (questionAskVo != null) {
            //todo 存草稿
            QaQuestions qaQuestions = new QaQuestions();
            qaQuestions.setTitle(questionAskVo.getTitle());
            qaQuestions.setDetailDoubt(questionAskVo.getContents());
            Destination destination = remoteDestinationService.getOne(questionAskVo.getDestId(), SecurityConstants.INNER).getData();
            if (destination != null) {
                qaQuestions.setDestinationName(destination.getName());
                qaQuestions.setAuthorId(SecurityUtils.getUserId());
                qaQuestions.setCreateTime(new Date());
                baseMapper.insert(qaQuestions);
            } else {
                throw new ServiceException("不存在这个目的地");
            }
        } else {
            throw new ServiceException("传入参数异常");
        }
    }

    @Override
    public QaQuestions getdetail(Long id) {
        if (id != null) {
            QaQuestions qaQuestions = baseMapper.selectById(id);
            List<Answer> answerList = answerService.getByQuestionId(id);
            for (Answer answer : answerList) {
                Long userId = answer.getUserId();
                UserInfo user = remoteUserInfoService.getOne(userId, SecurityConstants.INNER).getData();
                answer.setUser(user);
            }
            qaQuestions.setAnswerList(answerList);
            return qaQuestions;
        }else{
            throw new ServiceException("参数不合法");
        }
    }
//
//    @Override
//    public Answer addAnswer(Answer answer) {
//        answer.setCreateTime(LocalDateTime.now());
//        answer.setLikeCount(0);
//        answer.setLikeCount(0);
//        answer.setStatus(0);
//        lambdaQuery().eq()
//
//
//        return null;
//    }
}
