package cn.wolfcode.wolf2w.business.repository;

import cn.wolfcode.wolf2w.business.api.domain.QuestionEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuestionEsRepository extends ElasticsearchRepository<QuestionEs, Long> {
}
