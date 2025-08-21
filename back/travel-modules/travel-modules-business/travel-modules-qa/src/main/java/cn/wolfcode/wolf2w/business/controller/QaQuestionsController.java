package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.business.vo.QuestionAskVo;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.security.annotation.InnerAuth;
import cn.wolfcode.wolf2w.business.api.domain.QaQuestions;
import cn.wolfcode.wolf2w.business.query.QaQuestionsQuery;
import cn.wolfcode.wolf2w.business.service.IQaQuestionsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问答提问 Controller
 * 
 * @author yang
 * @date 2025-07-24
 */
@RestController
@RequestMapping("qaQuestions")
public class QaQuestionsController {
    @Autowired
    private IQaQuestionsService qaQuestionsService;

    @PostMapping("/create")
    public R<?> addQaQuestions(@RequestBody QuestionAskVo questionAskVo) {
        qaQuestionsService.addAsk(questionAskVo);
        return R.ok();
    }

    @PostMapping("/answer")
    public R<?> addAnswer(@RequestBody QuestionAskVo questionAskVo) {

        return R.ok();
    }



    /**
     * 问答提问详情
     */
    @GetMapping("/detail/{id}")
    public R<QaQuestions> detail(@PathVariable("id") Long id) {
        QaQuestions qaQuestions = qaQuestionsService.getdetail(id);
        return R.ok(qaQuestions);
    }
    /**
     * 问答提问 列表
     */
    @GetMapping("/page")
    public R<IPage<QaQuestions>> query( QaQuestionsQuery qo) {
        IPage<QaQuestions> page = qaQuestionsService.queryPage(qo);
        return R.ok(page);
    }


    /*****************************************对外暴露Fegin接口**********************************************/
    /**
     * Feign 接口
     */
    @GetMapping("/feign/list")
    public R<List<QaQuestions>> feignList() {
        return R.ok(qaQuestionsService.list());
    }
    @InnerAuth
    @GetMapping("/feign/{id}")
    public R<QaQuestions> feignGet(@PathVariable Long id) {
        return R.ok(qaQuestionsService.getById(id));
    }
}
