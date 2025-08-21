package cn.wolfcode.wolf2w.business.repository;

import cn.wolfcode.wolf2w.business.api.domain.StrategyEs;
import cn.wolfcode.wolf2w.business.api.domain.UserInfoEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInfoEsRepository extends ElasticsearchRepository<UserInfoEs,Long> {

}
