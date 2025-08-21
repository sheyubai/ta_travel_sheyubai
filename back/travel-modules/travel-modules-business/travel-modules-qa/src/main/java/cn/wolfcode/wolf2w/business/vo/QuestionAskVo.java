package cn.wolfcode.wolf2w.business.vo;

import lombok.Data;

@Data
public class QuestionAskVo {
    // 对应title
    private String title;
    // 对应detail_doubt
    private String contents;
    // 草稿标识，数据库表无，但业务逻辑中需要
    private Integer isDraft;
    //目的地id用来查目的地的名字放在数据库里
    private Long destId;
}
