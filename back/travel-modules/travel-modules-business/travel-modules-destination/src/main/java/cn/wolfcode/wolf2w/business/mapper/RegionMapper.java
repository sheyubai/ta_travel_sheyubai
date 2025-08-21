package cn.wolfcode.wolf2w.business.mapper;

import cn.wolfcode.wolf2w.business.api.domain.Region;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 区域Mapper接口
 * 
 * @author klq
 * @date 2025-07-22
 */
@Mapper
public interface RegionMapper extends BaseMapper<Region>{

}
