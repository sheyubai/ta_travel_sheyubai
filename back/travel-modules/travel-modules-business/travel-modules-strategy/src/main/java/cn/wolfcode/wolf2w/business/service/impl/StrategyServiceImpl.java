package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.RemoteDestinationService;
import cn.wolfcode.wolf2w.business.api.domain.*;
import cn.wolfcode.wolf2w.business.mapper.*;
import cn.wolfcode.wolf2w.business.query.StrategyQuery;
import cn.wolfcode.wolf2w.business.service.IStrategyConditionService;
import cn.wolfcode.wolf2w.business.service.IStrategyRankService;
import cn.wolfcode.wolf2w.business.service.IStrategyService;
import cn.wolfcode.wolf2w.business.util.DateUtil;
import cn.wolfcode.wolf2w.business.vo.CatalogVo;
import cn.wolfcode.wolf2w.business.vo.ThemeVO;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.core.utils.DateUtils;
import cn.wolfcode.wolf2w.common.rabbit.config.TravelRabiitConfig;
import cn.wolfcode.wolf2w.common.redis.service.RedisService;
import cn.wolfcode.wolf2w.common.redis.util.RedisKeys;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.C;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * 攻略Service业务层处理
 *
 * @author klq
 * @date 2025-07-22
 */
@Service
@Transactional
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements IStrategyService {
    @Autowired
    private StrategyCatalogMapper strategyCatalogMapper;
    @Autowired
    private StrategyThemeMapper strategyThemeMapper;
    @Autowired
    private StrategyContentMapper strategyContentMapper;
    @Autowired
    private IStrategyRankService strategyRankService;

    @Autowired
    private IStrategyConditionService strategyConditionService;


    @Autowired
    private RemoteDestinationService remoteDestinationService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitTemplate rabbitTemplate;






    @Override
    public IPage<Strategy> queryPage(StrategyQuery qo) {
        IPage<Strategy> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        LambdaQueryWrapper<Strategy> qw = new LambdaQueryWrapper<>();
        if (qo.getType() != null) {
            if (qo.getType() == 1 || qo.getType() == 2) {
                qw.eq(Strategy::getDestId, qo.getRefid());
            } else if (qo.getType() == 3) {
                qw.eq(Strategy::getThemeId, qo.getRefid());
            }
        }
        return baseMapper.selectPage(page, qw);
    }

    @Override
    public List<CatalogVo> queryCatalogVo(Long desteId) {
        LambdaQueryWrapper<StrategyCatalog> qw = new LambdaQueryWrapper<>();
        qw.eq(StrategyCatalog::getDestId, desteId);
        List<StrategyCatalog> list = strategyCatalogMapper.selectList(qw);
        List<CatalogVo> vos = new ArrayList<>();
        for (StrategyCatalog strategyCatalog : list) {
            CatalogVo vo = new CatalogVo();
            vo.setId(strategyCatalog.getId());
            vo.setName(strategyCatalog.getName());
            List<Strategy> strategies = lambdaQuery().eq(Strategy::getCatalogId, strategyCatalog.getId()).list();
            vo.setStrategies(strategies);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<Strategy> queryViewnnumTop3(Long destId) {
        return lambdaQuery().eq(Strategy::getDestId, destId).orderByDesc(Strategy::getId).last("limit 3").list();
    }

    @Override
    public int saveStrategy(Strategy strategy) {
        StrategyTheme strategyTheme = strategyThemeMapper.selectById(strategy.getThemeId());
        strategy.setThemeName(strategyTheme.getName());
        StrategyCatalog catalog = strategyCatalogMapper.selectById(strategy.getCatalogId());
        strategy.setCatalogName(catalog.getName());
        strategy.setDestId(catalog.getDestId());
        strategy.setDestName(catalog.getDestName());
        strategy.setCreateTime(new Date());
        //todo feign调用
        R<Long> ret = remoteDestinationService.isAbroad(catalog.getDestId(), SecurityConstants.INNER);
//        R<Long> ret = destinationController.isAbroad(catalog.getDestId());
        strategy.setIsabroad(ret.getData());
        strategy.setViewnum(0l);
        strategy.setFavornum(0l);
        strategy.setSharenum(0l);
        strategy.setReplynum(0l);
        strategy.setThumbsupnum(0l);
        baseMapper.insert(strategy);

        Long id = strategy.getId();
        String strategyContent = strategy.getContent().getContent();
        StrategyContent content = new StrategyContent();
        content.setId(id);
        content.setContent(strategyContent);
        int insert = strategyContentMapper.insert(content);

        //发送消息队列,消息内容就是攻略对象
//        String message = JSON.toJSONString(strategy);
//        rabbitTemplate.convertAndSend(TravelRabiitConfig.StrategyExchangeName,TravelRabiitConfig.StrategyKey,message);

        return insert;
    }

    @Override
    public void statisRank() {
        Date now = new Date();
        //国内排行

        saveRank(now, 1);
        saveRank(now, 2);
        saveRank(now, 3);
    }

    @Override
    public List<ThemeVO> queryTheme() {
        QueryWrapper<Strategy> qw = new QueryWrapper<>();
        qw.groupBy("theme_name");
//        qw.select("theme_name themeName, GROUP_CONCAT(DISTINCT dest_id) ids, GROUP_CONCAT(DISTINCT dest_name) names");
        qw.select("theme_name themeName, GROUP_CONCAT(DISTINCT dest_id) ids, GROUP_CONCAT(DISTINCT dest_name) names");
        List<Map<String, Object>> list = baseMapper.selectMaps(qw);

        List<ThemeVO> vos = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String themeName = (String) map.get("themeName");
            String ids = (String) map.get("ids");
            String names = (String) map.get("names");
            String[] idarr = ids.split(",");
            String[] namearr = names.split(",");
            List<Destination> destinations = new ArrayList<>();
            for (int i = 0; i < idarr.length; i++) {
                Long id = Long.valueOf(idarr[i]);
                String name = namearr[i];
                Destination destination = new Destination();
                destination.setId(id);
                destination.setName(name);
                destinations.add(destination);
            }
            ThemeVO themeVO = new ThemeVO(themeName, destinations);
            vos.add(themeVO);
        }
        return vos;
    }

    @Override
    public void statisCondition() {
        Date now = new Date();
        List<StrategyCondition> conditionList = new ArrayList<>();

        QueryWrapper<Strategy> qw = new QueryWrapper<>();
        qw.groupBy("dest_id", "dest_name");
        qw.select("dest_id id, dest_name name ,COUNT(*) count");
        qw.eq("isabroad", 1);
        List<Map<String, Object>> abroadlist = baseMapper.selectMaps(qw);

        qw.clear();
        qw.groupBy("dest_id", "dest_name");
        qw.select("dest_id id, dest_name name, COUNT(*) count");
        qw.eq("isabroad", 0);
        List<Map<String, Object>> chinalist = baseMapper.selectMaps(qw);

        qw.clear();
        qw.groupBy("theme_id", "theme_name");
        qw.select("theme_id id ,theme_name name ,COUNT(*) count");
        List<Map<String, Object>> themelist = baseMapper.selectMaps(qw);

        addtoconditionlist(abroadlist, now, conditionList, 1L);
        addtoconditionlist(chinalist, now, conditionList, 2L);
        addtoconditionlist(themelist, now, conditionList, 3L);
        strategyConditionService.saveBatch(conditionList);
    }

    private static void addtoconditionlist(List<Map<String, Object>> list, Date now, List<StrategyCondition> conditionList, Long type) {
        for (Map<String, Object> map : list) {
            long refid = (Long) map.get("id");
            String name = (String) map.get("name");
            Long count = (Long) map.get("count");
            StrategyCondition strategyCondition = new StrategyCondition();
            strategyCondition.setRefid(refid);
            strategyCondition.setType(type);
            strategyCondition.setName(name);
            strategyCondition.setCount(count);
            strategyCondition.setStatisTime(now);
            conditionList.add(strategyCondition);
        }
    }

    @Override
    public Map viewnumIncrease(Long sid) {
        //拼接key
        String key = strategyHashInit(sid);
        //阅读量加一
        redisService.incrementCacheMapValue(key, "viewnum", 1);
        //到redis中查询key对应的value并返回
        return redisService.getCacheMap(key);
    }

    @Override
    public Map favor(Long sid, Long userId) {
        String key = RedisKeys.STRATEGY_USER_FAVOR.join(userId.toString());
        if (!redisService.hasKey(key)) {
            Set<Long> sids = new HashSet<>();
            sids.add(-1l);
            redisService.setCacheSet(key, sids);
        }
        String hashKey = RedisKeys.STRATEGY_STATIS_HASH.join(sid.toString());
        boolean result = false;
        if (redisService.isCacheSetContains(key, sid)) {
            //已收藏
            //收藏数-1
            redisService.incrementCacheMapValue(hashKey, "favornum", -1);
            //从集合中移除收藏标志
            redisService.deleteCacheSetValue(key, sid);
        } else {
            //未收餐
            //收藏数+1
            redisService.incrementCacheMapValue(hashKey, "favornum", 1);
            //在集合中加入收藏标志
            redisService.addCacheSetValue(key, sid);
            result = true;
        }
        Map<String, Object> cacheMap = redisService.getCacheMap(hashKey);
        cacheMap.put("result", result);
        return cacheMap;
    }

    @Override
    public Map thumbsup(Long sid, Long userId) {
        String key = RedisKeys.STRATEGY_USER_THUMBSUP.join(sid.toString(), userId.toString());
        if (!redisService.hasKey(key)) {
            Date now = new Date();
            Date endDate = DateUtil.getEndDate(now);
            long time = DateUtil.getDateBetween(now, endDate);

            redisService.setCacheObject(key, 0, time == 0 ? 2 : time, TimeUnit.SECONDS);
        }

        //返回值是自增后redis存的点赞次数
        Long ret = redisService.incrementCacheObjectValue(key, 1);

        //攻略数据据存在redis中的key
        String hashkey = RedisKeys.STRATEGY_STATIS_HASH.join(sid.toString());
        //要求每天能点5个赞
        if (ret > 5) {
            //点赞失败
            Map<String, Object> cacheMap = redisService.getCacheMap(hashkey);
            cacheMap.put("result", false);
            return cacheMap;
        } else {
            //点赞成功
            redisService.incrementCacheMapValue(hashkey, "thumbsupnum", 1);
            Map<String, Object> cacheMap = redisService.getCacheMap(hashkey);
            cacheMap.put("result", true);
            return cacheMap;
        }
    }

    @Override
    public boolean isuserfavor(Long uid, Long sid) {
        String key = RedisKeys.STRATEGY_USER_FAVOR.join(uid.toString());
        return redisService.isCacheSetContains(key, sid);
    }

    @Override
    public Map replynumincr(Long sid) {
        String key = strategyHashInit(sid);
        redisService.incrementCacheMapValue(key, "replynum", 1);
        return redisService.getCacheMap(key);
    }

    @Override
    public void statisHashPersistence() {
        //查询redis中所有数据
        String strategyStatisKey = RedisKeys.STRATEGY_STATIS_HASH.join("*");
        Collection<String> keys = redisService.keys(strategyStatisKey);
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Map<String, Object> cacheMap = redisService.getCacheMap(key);
                lambdaUpdate().eq(Strategy::getId,cacheMap.get("strategyId"))
                        .set(Strategy::getReplynum,cacheMap.get("replynum"))
                        .set(Strategy::getViewnum,cacheMap.get("viewnum"))
                        .set(Strategy::getFavornum,cacheMap.get("favornum"))
                        .set(Strategy::getSharenum,cacheMap.get("sharenum"))
                        .set(Strategy::getThumbsupnum,cacheMap.get("thumbsupnum"))
                        .update();
            }
        }
        //存到mysql中


    }

    private String strategyHashInit(Long sid) {
        String key = RedisKeys.STRATEGY_STATIS_HASH.join(sid.toString());
        //判断redis是否有这个key,如果没有创建value存到这个key里
        if (!redisService.hasKey(key)) {
            Strategy strategy = baseMapper.selectById(sid);
            Map map = new HashMap();
            map.put("strategyId", strategy.getId());
            map.put("viewnum", Integer.valueOf(strategy.getViewnum().toString()));
            map.put("replynum", Integer.valueOf(strategy.getReplynum().toString()));
            map.put("favornum", Integer.valueOf(strategy.getFavornum().toString()));
            map.put("sharenum", Integer.valueOf(strategy.getSharenum().toString()));
            map.put("thumsupnum", Integer.valueOf(strategy.getThumbsupnum().toString()));
            redisService.setCacheMap(key, map);
        }
        return key;
    }


    private void saveRank(Date now, long type) {
        List<Strategy> list = null;
        if (type == 1) {
            //CTRL+ALT+M
            list = lambdaQuery().eq(Strategy::getIsabroad, 1).orderByDesc(Strategy::getViewnum).last("limit 10").list();
        } else if (type == 2) {
            //CTRL+ALT+M
            list = lambdaQuery().eq(Strategy::getIsabroad, 0).orderByDesc(Strategy::getViewnum).last("limit 10").list();
        } else if (type == 3) {
            //CTRL+ALT+M
            list = lambdaQuery().orderByDesc(Strategy::getViewnum).last("limit 10").list();
        }

        List<StrategyRank> ranks = new ArrayList<>();
        for (Strategy strategy : list) {
            StrategyRank rank = new StrategyRank();
            rank.setStrategyId(strategy.getId());
            rank.setStrategyTitle(strategy.getTitle());
            rank.setDestId(strategy.getDestId());
            rank.setDestName(strategy.getDestName());
            rank.setType(type);
            rank.setStatisnum(strategy.getViewnum());
            rank.setStatisTime(now);
            ranks.add(rank);
        }
        strategyRankService.saveBatch(ranks);
    }
}
