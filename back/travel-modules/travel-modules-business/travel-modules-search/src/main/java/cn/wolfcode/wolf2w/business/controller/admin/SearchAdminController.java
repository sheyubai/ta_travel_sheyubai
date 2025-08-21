package cn.wolfcode.wolf2w.business.controller.admin;

import cn.wolfcode.wolf2w.business.repository.QuestionEsRepository;
import cn.wolfcode.wolf2w.business.service.*;
import cn.wolfcode.wolf2w.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchAdminController {

    @Autowired
    private IStrategyEsService strategyEsService;
    @Autowired
    private IUserInfoEsService userInfoEsService;
    @Autowired
    private IDestinationEsService destinationEsService;
    @Autowired
    private INoteEsService noteEsService;
    @Autowired
    private IQuestionEsService questionEsService;


    @GetMapping("/dataInit")
    public R<?> dataInit() {
        questionEsService.initData();
        strategyEsService.initData();
        userInfoEsService.initData();
        destinationEsService.initData();
        noteEsService.initData();
        return R.ok("实现Es数据初始化完成");
    }
}
