package cn.wolfcode.wolf2w.business.query;


import cn.wolfcode.wolf2w.common.core.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
* 状态，0表示待发布，1表示发布查询参数封装对象
*/
@Setter
@Getter
public class StrategyQuery extends  QueryObject{
    private Long type;// 1 国内  2 国外  3主题
    private Long refid;//当type=1或type=2时表示目的地id; 当type=3时refid表示主题（ta_strategy_theme） id
    
}
