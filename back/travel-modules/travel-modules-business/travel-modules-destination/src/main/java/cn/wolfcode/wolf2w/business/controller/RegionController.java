package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.security.annotation.InnerAuth;
import cn.wolfcode.wolf2w.business.api.domain.Region;
import cn.wolfcode.wolf2w.business.query.RegionQuery;
import cn.wolfcode.wolf2w.business.service.IRegionService;
import cn.wolfcode.wolf2w.common.security.annotation.RequiresLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Target;
import java.util.List;

/**
 * 区域 Controller
 * 
 * @author klq
 * @date 2025-07-22
 */

@Api(value = "热门接口类" ,tags = "对外fegin封装接口")
@RestController
@RequestMapping("regions")
@ApiResponses(
        {
                @ApiResponse(code = 200, message = "成功"),
                @ApiResponse(code = 400, message = "参数异常"),
                @ApiResponse(code = 401, message = "未认证"),
                @ApiResponse(code = 403, message = "未授权"),
                @ApiResponse(code = 404, message = "无页面"),
                @ApiResponse(code = 500, message = "服务器内部错误")
        }
)
public class RegionController {
    @Autowired
    private IRegionService regionService;



    @GetMapping("/testauth")
    @RequiresLogin
    public R testauth() {
        return R.ok();
    }

    /**
     * 区域详情
     */
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id",value = "详情id",readOnly = true,dataType = "Long",example = "5L")
    )
    @ApiOperation(value = "热门区域详情查询",notes = "根据区域id查询区域详情")
    @GetMapping("/detail/{id}")
    public R<Region> detail(@PathVariable Long id) {
        Region region = regionService.getById(id);
        return R.ok(region);
    }

    /**
     * 区域 列表
     */
    @ApiOperation(value = "热门区域分页查询",notes = "热门区域分页查询参数为分页对象")
    @GetMapping("/query")
    public R<IPage<Region>> query(RegionQuery qo) {
        IPage<Region> page = regionService.queryPage(qo);
        return R.ok(page);
    }


    /*****************************************对外暴露Fegin接口**********************************************/
    /**
     * Feign 接口
     */
    @GetMapping("/feign/list")
    public R<List<Region>> feignList() {
        return R.ok(regionService.list());
    }
    @InnerAuth
    @GetMapping("/feign/{id}")
    public R<Region> feignGet(@PathVariable Long id) {
        return R.ok(regionService.getById(id));
    }
}
