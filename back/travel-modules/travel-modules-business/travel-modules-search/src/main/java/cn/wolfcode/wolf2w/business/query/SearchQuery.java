package cn.wolfcode.wolf2w.business.query;

import cn.wolfcode.wolf2w.common.core.query.QueryObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.query.BaseQuery;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuery extends QueryObject {
    private Integer type=-1;
}
