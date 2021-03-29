package cc.ryanc.halo.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *     待办事项
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Data
@Entity
@Table(name = "halo_backlog")
public class Backlog implements Serializable {

    private static final long serialVersionUID = -5144055068797033748L;

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Long backlogId;

    /**
     * 待办名字
     */
    private String backlogName;

    /**
     * 阅读状态 0=未读  1=已读 2=已读5秒以上
     */
    private Integer backlogStatus = 0;

    /**
     * 新建日期
     */
    private Date backlogDate;

    /**
     * 最后一次更新时间
     */
    private Date backlogUpdate;

    /**
     * 待办文章 多对一
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 白名单用户 多对一
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "white_id")
    private White white;
}
