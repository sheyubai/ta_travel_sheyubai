package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.security.annotation.InnerAuth;
import cn.wolfcode.wolf2w.business.api.domain.StrategyCatalog;
import cn.wolfcode.wolf2w.business.query.StrategyCatalogQuery;
import cn.wolfcode.wolf2w.business.service.IStrategyCatalogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 攻略分类 Controller
 * 
 * @author klq
 * @date 2025-07-22
 */
@RestController
@RequestMapping("strategyCatalogs")
public class StrategyCatalogController {
    @Autowired
    private IStrategyCatalogService strategyCatalogService;
    /**
     * 攻略分类详情
     */
    @GetMapping("/detail/{id}")
    public R<StrategyCatalog> detail(@PathVariable Long id) {
        StrategyCatalog strategyCatalog = strategyCatalogService.getById(id);
        return R.ok(strategyCatalog);
    }
    /**
     * 攻略分类 列表
     */
    @GetMapping("/query")
    public R<IPage<StrategyCatalog>> query(StrategyCatalogQuery qo) {
        IPage<StrategyCatalog> page = strategyCatalogService.queryPage(qo);
        return R.ok(page);
    }


    /*****************************************对外暴露Fegin接口**********************************************/
    /**
     * Feign 接口
     */
    @GetMapping("/feign/list")
    public R<List<StrategyCatalog>> feignList() {
        return R.ok(strategyCatalogService.list());
    }
    @InnerAuth
    @GetMapping("/feign/{id}")
    public R<StrategyCatalog> feignGet(@PathVariable Long id) {
        return R.ok(strategyCatalogService.getById(id));
    }
}
