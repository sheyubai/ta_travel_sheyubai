package cn.wolfcode.wolf2w.business.service.impl;


import cn.wolfcode.wolf2w.business.api.RemoteQaQuestionsService;
import cn.wolfcode.wolf2w.business.api.domain.QaQuestions;
import cn.wolfcode.wolf2w.business.api.domain.QuestionEs;

import cn.wolfcode.wolf2w.business.repository.QuestionEsRepository;

import cn.wolfcode.wolf2w.business.service.IQuestionEsService;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionEsServiceImpl implements IQuestionEsService {
    @Autowired
    private QuestionEsRepository repository;
    @Autowired
    private RemoteQaQuestionsService remoteQaQuestionService;

    @Override
    public void initData() {
        List<QaQuestions> list = remoteQaQuestionService.list(SecurityConstants.INNER).getData();
        List<QuestionEs>eslist=new ArrayList<>();
        for (QaQuestions QaQuestion : list) {
            QuestionEs es = new QuestionEs();
            BeanUtils.copyProperties(QaQuestion, es);
            eslist.add(es);
        }
        repository.saveAll(eslist);
    }
}
