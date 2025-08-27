package cn.wolfcode.wolf2w.business.api.domain;

import cn.wolfcode.wolf2w.member.api.domain.UserInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.wolfcode.wolf2w.common.core.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问题回答对象 qa_answer
 *
 * @author yang
 * @date 2025-08-12
 */
@Data
@TableName("qa_answer")
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属问题ID
     */
    @Excel(name = "所属问题ID")
    private Long questionId;

    /**
     * 所属回答ID，若是评论回答则填，否则为空
     */
    @Excel(name = "所属回答ID，若是评论回答则填，否则为空")
    private Long answerId;

    /**
     * 评论用户ID
     */
    @Excel(name = "评论用户ID")
    private Long userId;

    /**
     * 评论内容
     */
    @Excel(name = "评论内容")
    private String content;

    /**
     * 回复的评论ID，NULL或0表示顶级评论
     */
    @Excel(name = "回复的评论ID，NULL或0表示顶级评论")
    private Long refId;

    /**
     * 状态：0正常，1审核，2删除
     */
    @Excel(name = "状态：0正常，1审核，2删除")
    private Integer status;

    /**
     * 点赞数
     */
    @Excel(name = "点赞数")
    private Integer likeCount;

    private LocalDateTime createTime;

    // 是否收藏
    private Integer isCollect;

    // 回答封面图片地址
    private String coverUrl;

    @TableField(exist = false)
    private UserInfo user;

}
