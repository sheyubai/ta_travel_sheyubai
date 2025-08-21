package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.security.annotation.InnerAuth;
import cn.wolfcode.wolf2w.business.api.domain.StrategyContent;
import cn.wolfcode.wolf2w.business.query.StrategyContentQuery;
import cn.wolfcode.wolf2w.business.service.IStrategyContentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 攻略内容 Controller
 * 
 * @author klq
 * @date 2025-07-22
 */
@RestController
@RequestMapping("strategyContents")
public class StrategyContentController {
    @Autowired
    private IStrategyContentService strategyContentService;
    /**
     * 攻略内容详情
     */
    @GetMapping("/detail/{id}")
    public R<StrategyContent> detail(@PathVariable Long id) {
        StrategyContent strategyContent = strategyContentService.getById(id);
        return R.ok(strategyContent);
    }
    /**
     * 攻略内容 列表
     */
    @GetMapping("/query")
    public R<IPage<StrategyContent>> query(StrategyContentQuery qo) {
        IPage<StrategyContent> page = strategyContentService.queryPage(qo);
        return R.ok(page);
    }


    /*****************************************对外暴露Fegin接口**********************************************/
    /**
     * Feign 接口
     */
    @GetMapping("/feign/list")
    public R<List<StrategyContent>> feignList() {
        return R.ok(strategyContentService.list());
    }
    @InnerAuth
    @GetMapping("/feign/{id}")
    public R<StrategyContent> feignGet(@PathVariable Long id) {
        return R.ok(strategyContentService.getById(id));
    }
}
