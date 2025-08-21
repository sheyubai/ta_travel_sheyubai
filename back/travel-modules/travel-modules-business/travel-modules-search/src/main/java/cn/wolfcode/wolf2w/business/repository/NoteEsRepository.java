package cn.wolfcode.wolf2w.business.repository;

import cn.wolfcode.wolf2w.business.api.domain.NoteEs;
import cn.wolfcode.wolf2w.business.api.domain.StrategyEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NoteEsRepository extends ElasticsearchRepository<NoteEs,Long> {
}
