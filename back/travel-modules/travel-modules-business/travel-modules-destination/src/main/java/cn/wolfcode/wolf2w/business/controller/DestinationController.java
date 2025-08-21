package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.business.api.domain.Region;
import cn.wolfcode.wolf2w.business.service.IRegionService;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.security.annotation.InnerAuth;
import cn.wolfcode.wolf2w.business.api.domain.Destination;
import cn.wolfcode.wolf2w.business.query.DestinationQuery;
import cn.wolfcode.wolf2w.business.service.IDestinationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 目的地 Controller
 * 
 * @author klq
 * @date 2025-07-22
 */

@Slf4j
@RestController
@RequestMapping("destinations")
public class DestinationController {
    @Autowired
    private IDestinationService destinationService;
    @Autowired
    private IRegionService regionService;

    @GetMapping(value = "/list")
    public R<List<Destination>> list() {
        List<Destination> list = destinationService.list();
        return R.ok(list);
    }
    /*问答的目的地*/
    @GetMapping(value = "/list", params = "type")
    public R<List<Destination>> QaDestinationList(@RequestParam Long type) {
        List<Destination> list= destinationService.getQaAskList(type);
        log.info("这是问答的目的地查询");
        return R.ok(list);
    }



    @GetMapping("/toasts")
    public R<List<Destination>> toasts(Long destId) {
        List<Destination> list=destinationService.queryToasts(destId);
        return R.ok(list);
    }

    /**
     * 目的地址二级菜单
     * @param regionId
     * @return
     */
    @GetMapping("/search")
    public R<List<Destination>> search(Long regionId) {
       List<Destination> list = destinationService.queryByRegionId(regionId);
       return R.ok(list);
    }
    /**
     * 目的地址菜单
     */
    @GetMapping("/regions")
    public R<List<Region>> queryRegions(Long ishot) {
        List<Region> list=regionService.queryRegion(ishot);
        return R.ok(list);
    }


    /**
     * 目的地详情
     */
    @GetMapping("/detail/{id}")
    public R<Destination> detail(@PathVariable Long id) {
        Destination destination = destinationService.getById(id);
        return R.ok(destination);
    }
    /**
     * 目的地 列表
     */
    @GetMapping("/query")
    public R<IPage<Destination>> query(DestinationQuery qo) {
        IPage<Destination> page = destinationService.queryPage(qo);
        return R.ok(page);
    }


    /*****************************************对外暴露Fegin接口**********************************************/
    /**
     * Feign 接口
     */
    @GetMapping("/feign/list")
    public R<List<Destination>> feignList() {
        return R.ok(destinationService.list());
    }
//    @InnerAuth
    @GetMapping("/feign/{id}")
    public R<Destination> feignGet(@PathVariable Long id) {
        return R.ok(destinationService.getById(id));
    }

    @GetMapping("/isAbroad/{destId}")
    public R<Long> isAbroad(@PathVariable("destId") Long destId){
        List<Destination> list = destinationService.queryToasts(destId);
        return R.ok("中国".equals(list.get(0).getName())? 0l :1l);
    }
}
