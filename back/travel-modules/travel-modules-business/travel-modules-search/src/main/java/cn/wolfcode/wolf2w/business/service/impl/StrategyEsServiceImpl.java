package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.RemoteStrategyService;
import cn.wolfcode.wolf2w.business.api.domain.Strategy;
import cn.wolfcode.wolf2w.business.api.domain.StrategyEs;
import cn.wolfcode.wolf2w.business.repository.StrategyEsRepository;
import cn.wolfcode.wolf2w.business.service.IStrategyEsService;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

@Service
public class StrategyEsServiceImpl implements IStrategyEsService {
    @Autowired
    private StrategyEsRepository repository;
    @Autowired
    private RemoteStrategyService remoteStrategyService;



    @Override
    public void initData() {
        List<Strategy> list = remoteStrategyService.list(SecurityConstants.INNER).getData();
        List<StrategyEs>eslist=new ArrayList<>();
        for (Strategy strategy : list) {
            StrategyEs es = new StrategyEs();
            BeanUtils.copyProperties(strategy, es);
            eslist.add(es);
        }
        repository.saveAll(eslist);
    }
}
