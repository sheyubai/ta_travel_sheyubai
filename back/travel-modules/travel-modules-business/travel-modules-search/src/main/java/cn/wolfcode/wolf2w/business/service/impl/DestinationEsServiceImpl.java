package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.RemoteDestinationService;
import cn.wolfcode.wolf2w.business.api.domain.Destination;
import cn.wolfcode.wolf2w.business.api.domain.DestinationEs;
import cn.wolfcode.wolf2w.business.repository.DestinationEsRepository;
import cn.wolfcode.wolf2w.business.service.IDestinationEsService;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DestinationEsServiceImpl implements IDestinationEsService {
    @Autowired
    private DestinationEsRepository repository;
    @Autowired
    private RemoteDestinationService remoteDestinationService;

    @Override
    public void initData() {
        List<Destination> list = remoteDestinationService.list(SecurityConstants.INNER).getData();
        List<DestinationEs>eslist=new ArrayList<>();
        for (Destination destination : list) {
            DestinationEs es = new DestinationEs();
            BeanUtils.copyProperties(destination, es);
            eslist.add(es);
        }
        repository.saveAll(eslist);
    }
}
