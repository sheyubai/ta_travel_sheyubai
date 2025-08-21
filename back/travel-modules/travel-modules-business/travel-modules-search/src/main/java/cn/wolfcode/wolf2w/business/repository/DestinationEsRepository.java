package cn.wolfcode.wolf2w.business.repository;

import cn.wolfcode.wolf2w.business.api.domain.DestinationEs;
import cn.wolfcode.wolf2w.business.api.domain.StrategyEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DestinationEsRepository extends ElasticsearchRepository<DestinationEs,Long> {
}
