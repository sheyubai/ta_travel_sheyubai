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
import java.util.Date;
import java.util.List;

/**
 * 问答提问对象 qa_questions
 * 
 * @author yang
 * @date 2025-07-24
 */
@Data
@Accessors(chain = true)
@TableName("qa_questions")
public class QaQuestions implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 问题ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 问题标题 */
    @Excel(name = "问题标题")
    private String title;

    /** 问题内容 */
    @Excel(name = "问题内容")
    private String detailDoubt;

    /** 作者ID */
    @Excel(name = "作者ID")
    private Long authorId;

    /** 封面图URL */
    @Excel(name = "封面图URL")
    private String coverUrl;

    /** 标签（用逗号分隔） */
    @Excel(name = "标签", readConverterExp = "用=逗号分隔")
    private String labelList;

    /** 目的地名称 */
    @Excel(name = "目的地名称")
    private String destinationName;

    /** 问题分类（0-热门 1-最新 2-待回答） */
    @Excel(name = "问题分类", readConverterExp = "0=-热门,1=-最新,2=-待回答")
    private Integer category;

    /** 评论数 */
    @Excel(name = "评论数")
    private Long commentCount;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likeCount;

    @Excel
    private Date createTime;

    @TableField(exist = false)
    private String[] AllLabelList;

    @TableField(exist = false)
    private UserInfo user;

    @TableField(exist = false)
    private List<Answer> answerList;

}
