package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.domain.Destination;
import cn.wolfcode.wolf2w.business.api.domain.Region;
import cn.wolfcode.wolf2w.business.mapper.DestinationMapper;
import cn.wolfcode.wolf2w.business.mapper.RegionMapper;
import cn.wolfcode.wolf2w.business.query.DestinationQuery;
import cn.wolfcode.wolf2w.business.service.IDestinationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 目的地Service业务层处理
 *
 * @author klq
 * @date 2025-07-22
 */
@Service
@Transactional
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements IDestinationService {
    @Autowired
    private RegionMapper regionMapper;

    @Override
    public IPage<Destination> queryPage(DestinationQuery qo) {
        IPage<Destination> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        return lambdaQuery()
                .page(page);
    }

    @Override
    public List<Destination> queryByRegionId(Long regionId) {
        List<Destination> list = null;
        if (regionId == -1) {
            //国内
            list = lambdaQuery().eq(Destination::getParentId, 1).list();
        } else {
            Region region = regionMapper.selectById(regionId);
            String refIds = region.getRefIds();
            String[] split = refIds.split(",");
            List<Long> ids = new ArrayList<>();
            for (String s : split) {
                Long id = Long.valueOf(s);
                ids.add(id);
            }
            list = baseMapper.selectBatchIds(ids);

        }
        for (Destination destination : list) {
            Long id = destination.getId();
//            List<Destination> children = lambdaQuery().eq(Destination::getParentId, id).list();
            //select*fromm ..where parentId =id;
            List<Destination> children = lambdaQuery().eq(Destination::getParentId, id)
                    .last("limit 10").list();
            destination.setChildren(children);
        }
        return list;
    }



    @Override
    public List<Destination> getQaAskList(Long type) {
        //热门
        if (type == 2) {
            List<Destination> list = lambdaQuery().orderByAsc(Destination::getId).last("limit 10").list();
            return list;
        }
        //国内
        else if (type == -1) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id",1);
            List<Destination> list = baseMapper.selectList(queryWrapper);
            for (Destination destination : list) {
                destination.setChildren(childrengetlist(destination.getId()));
            }
            return list;
        }
        //国际
        else if (type == 1) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.isNull("parent_id");
//            queryWrapper.ne("parent_id", 1);
            List<Destination> list = baseMapper.selectList(queryWrapper);
            for (Destination destination : list) {
                destination.setChildren(childrengetlist(destination.getId()));
            }
            return list;
        }
        return null;
    }
    private List<Destination> childrengetlist(Long parentId) {
        List<Destination> list = lambdaQuery().eq(Destination::getParentId, parentId).list();
        if (list != null) {
            for (Destination destination : list) {
                destination.setChildren(childrengetlist(destination.getId()));
            }
        }
        return list;

    }


    @Override
    public List<Destination> queryToasts(Long destId) {
        List<Destination> list = new ArrayList<>();
        createToasts(destId, list);
        return list;
    }
    private void createToasts(Long destId, List<Destination> list) {
        if (destId == null) {
            return;
        }
        Destination destination = baseMapper.selectById(destId);
        Long parentId = destination.getParentId();

        createToasts(parentId, list);
        list.add(destination);
    }
}
