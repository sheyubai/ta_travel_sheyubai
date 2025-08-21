package cn.wolfcode.wolf2w.business.api.domain;

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

/**
 * 问答用户对象 qa_users
 * 
 * @author ruoyi
 * @date 2025-07-24
 */
@Data
@Accessors(chain = true)
@TableName("qa_users")
public class QaUsers implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 用户ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 等级 */
    @Excel(name = "等级")
    private Long level;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String avatarUrl;



}
