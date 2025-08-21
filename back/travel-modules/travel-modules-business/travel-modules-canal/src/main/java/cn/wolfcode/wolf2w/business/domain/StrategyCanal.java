package cn.wolfcode.wolf2w.business.domain;

import cn.wolfcode.wolf2w.common.core.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 攻略对象 ta_strategy
 *
 * @author klq
 * @date 2025-07-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StrategyCanal {

    /**
     * 主键
     */

    private Long id;

    /**
     * 引用目的地ID
     */
    @Column(name="dest_id")
    private Long destId;

    /**
     * 引用目的地名称
     */
    @Column(name="dest_name")
    private String destName;

    /**
     * 引用主题ID
     */
    @Column(name="theme_id")
    private Long themeId;

    /**
     * 引用主题名称
     */
    @Column(name="theme_name")
    private String themeName;

    /**
     * 引用分类ID
     */
    @Column(name="catalog_id")
    private Long catalogId;

    /**
     * 引用分类名称
     */
    @Column(name="catalog_name")
    private String catalogName;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    @Column(name="sub_title")
    private String subTitle;

    /**
     * 内容概要
     */
    private String summary;

    /**
     * 封面图片地址
     */
    @Column(name="cover_url")
    private String coverUrl;

    /**
     * 是否为国外，0表示国内，1表示国外
     */
    private Long isabroad;

    /**
     * 点击数
     */
    private Long viewnum;

    /**
     * 攻略评论数
     */
    private Long replynum;

    /**
     * 收藏数
     */
    private Long favornum;

    /**
     * 分享数
     */
    private Long sharenum;

    /**
     * 点赞数
     */
    private Long thumbsupnum;

    /**
     * 状态，0表示待发布，1表示发布
     */
    private Long state;

    @Column(name="create_time")
    private Date createTime;
}
