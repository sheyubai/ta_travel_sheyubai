package cn.wolfcode.wolf2w.business.vo;

import cn.wolfcode.wolf2w.business.api.domain.Strategy;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CatalogVo {
    public Long id;
    private String name;
    private List<Strategy> strategies=new ArrayList<>();
}
