package cn.wolfcode.wolf2w.business.service;

import cn.wolfcode.wolf2w.business.api.domain.Region;
import cn.wolfcode.wolf2w.business.query.RegionQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 区域Service接口
 * 
 * @author klq
 * @date 2025-07-22
 */
public interface IRegionService extends IService<Region>{
    /**
    * 分页
    * @param qo
    * @return
    */
    IPage<Region> queryPage(RegionQuery qo);

    List<Region> queryRegion(Long ishot);
}
