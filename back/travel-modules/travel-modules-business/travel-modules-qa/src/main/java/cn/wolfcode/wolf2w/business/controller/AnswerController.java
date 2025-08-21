package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.business.api.domain.Answer;
import cn.wolfcode.wolf2w.business.query.AnswerQuery;
import cn.wolfcode.wolf2w.business.service.IAnswerService;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.security.annotation.InnerAuth;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问题回答 Controller
 * 
 * @author yang
 * @date 2025-08-12
 */
@RestController
@RequestMapping("answer")
public class AnswerController {
    @Autowired
    private IAnswerService answerService;
    /**
     * 问题回答详情
     */
    @GetMapping("/detail/{id}")
    public R<Answer> detail(@PathVariable("id") Long id) {
        Answer answer = answerService.getById(id);
        return R.ok(answer);
    }
    /**
     * 问题回答 列表
     */
    @GetMapping("/query")
    public R<IPage<Answer>> query(AnswerQuery qo) {
        IPage<Answer> page = answerService.queryPage(qo);
        return R.ok(page);
    }

    @PostMapping("/like/{id}")
    public R<?> like(@PathVariable("id") Long id) {



        return R.ok("1111");
    }


    /*****************************************对外暴露Fegin接口**********************************************/
    /**
     * Feign 接口
     */
    @GetMapping("/feign/list")
    public R<List<Answer>> feignList() {
        return R.ok(answerService.list());
    }
    @InnerAuth
    @GetMapping("/feign/{id}")
    public R<Answer> feignGet(@PathVariable Long id) {
        return R.ok(answerService.getById(id));
    }
}
