package cn.wolfcode.wolf2w.business.service.impl;


import cn.wolfcode.wolf2w.business.api.domain.UserInfoEs;


import cn.wolfcode.wolf2w.business.repository.UserInfoEsRepository;

import cn.wolfcode.wolf2w.business.service.IUserInfoEsService;

import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.member.api.RemoteUserInfoService;
import cn.wolfcode.wolf2w.member.api.domain.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoEsServiceImpl implements IUserInfoEsService {
    @Autowired
    private UserInfoEsRepository repository;
    @Autowired
    private RemoteUserInfoService remoteUserinfoService;


    @Override
    public void initData() {
        List<UserInfo> list = remoteUserinfoService.list(SecurityConstants.INNER).getData();
        List<UserInfoEs> eslist = new ArrayList<>();
        for (UserInfo userinfo : list) {
            UserInfoEs es = new UserInfoEs();
            BeanUtils.copyProperties(userinfo, es);
            eslist.add(es);
        }
        repository.saveAll(eslist);
    }
}
