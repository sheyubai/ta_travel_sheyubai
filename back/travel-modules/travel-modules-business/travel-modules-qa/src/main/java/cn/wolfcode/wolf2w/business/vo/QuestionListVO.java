package cn.wolfcode.wolf2w.business.vo;

import cn.wolfcode.wolf2w.business.api.domain.QaQuestions;
import cn.wolfcode.wolf2w.business.api.domain.QaUsers;
import lombok.Data;

@Data
public class QuestionListVO {
    private QaQuestions Questions;
    private QaUsers Users;
}
