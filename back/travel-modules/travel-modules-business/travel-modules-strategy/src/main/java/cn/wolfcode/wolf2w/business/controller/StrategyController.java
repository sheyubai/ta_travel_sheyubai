package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.business.api.domain.StrategyContent;
import cn.wolfcode.wolf2w.business.service.IStrategyContentService;
import cn.wolfcode.wolf2w.business.vo.CatalogVo;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.context.SecurityContextHolder;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.security.annotation.InnerAuth;
import cn.wolfcode.wolf2w.business.api.domain.Strategy;
import cn.wolfcode.wolf2w.business.query.StrategyQuery;
import cn.wolfcode.wolf2w.business.service.IStrategyService;
import cn.wolfcode.wolf2w.common.security.annotation.RequiresLogin;
import cn.wolfcode.wolf2w.common.security.annotation.RequiresPermissions;
import cn.wolfcode.wolf2w.common.security.annotation.RequiresRoles;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 攻略 Controller
 *
 * @author klq
 * @date 2025-07-22
 */
@RestController
@RequestMapping("strategies")
public class StrategyController {
    @Autowired
    private IStrategyService strategyService;
    @Autowired
    private IStrategyContentService strategyContentService;


    @PostMapping("/replynumIncr")
    public R replynumIncr(Long sid) {
        Map map=strategyService.replynumincr(sid);
        return R.ok(map);
    }

    @GetMapping("/isUserFavor")
    public R<?> isUserFavor(Long uid,Long sid) {
        boolean ret=strategyService.isuserfavor(uid,sid);
        return R.ok(ret);
    }

    @RequiresLogin
    @PostMapping("/thumbsup")
    public R<?> thumbsup(Long sid){
        Long userId = SecurityContextHolder.getUserId();
        Map map =strategyService.thumbsup(sid,userId);
        return R.ok(map);
    }

    @RequiresLogin//登录才能访问资源
    @PostMapping("/favor")
    public R<?> favor(Long sid){
        Long userId = SecurityContextHolder.getUserId();
        Map map = strategyService.favor(sid,userId);
        return R.ok(map);
    }

    @PostMapping("/viewnumIncr")
    public R<?> viewnumIncr(Long sid){
     Map map= strategyService.viewnumIncrease(sid);
     return R.ok(map);
    }

    @GetMapping("/viewnnumTop3")
    public R<List<Strategy>> viewnnumTop3(Long destId) {
        List<Strategy> list = strategyService.queryViewnnumTop3(destId);
        return R.ok(list);
    }

    @GetMapping("/catalogList")
    public R<List<CatalogVo>> catalogList(Long destId) {
        List<CatalogVo> list = strategyService.queryCatalogVo(destId);
        return R.ok(list);
    }

    /**
     * 攻略详情
     */
    @GetMapping("/detail/{id}")
    public R<Strategy> detail(@PathVariable Long id) {
        Strategy strategy = strategyService.getById(id);
        StrategyContent content = strategyContentService.getById(id);
        strategy.setContent(content);
        return R.ok(strategy);
    }

    /**
     * 攻略 列表
     */
    @GetMapping("/query")
    public R<IPage<Strategy>> query(StrategyQuery qo) {
        IPage<Strategy> page = strategyService.queryPage(qo);
        return R.ok(page);
    }


    /*****************************************对外暴露Fegin接口**********************************************/
    /**
     * Feign 接口
     */
    @GetMapping("/feign/list")
    public R<List<Strategy>> feignList() {
        return R.ok(strategyService.list());
    }

    @InnerAuth
    @GetMapping("/feign/{id}")
    public R<Strategy> feignGet(@PathVariable Long id) {
        return R.ok(strategyService.getById(id));
    }

    @PostMapping("/feign/statisRank")
    public R<?> statisRank() {
        System.out.println("攻略服务的==statisRank方法被调用");
        strategyService.statisRank();
        return R.ok();
    }

    @PostMapping("/feign/statisCondition")
    public R<?> statisCondition(){
        System.out.println("攻略服务被调用");
        strategyService.statisCondition();
        return R.ok();
    }

    @PostMapping("/feign/statisHashPersistence")
    public R<?> statisHashPersistence(){
        System.out.println("持久化");
        strategyService.statisHashPersistence();
        return R.ok();
    }


}
