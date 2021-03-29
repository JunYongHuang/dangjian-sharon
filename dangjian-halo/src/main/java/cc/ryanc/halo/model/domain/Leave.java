package cc.ryanc.halo.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *     请假单
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Data
@Entity
@Table(name = "halo_leave")
public class Leave implements Serializable {

    private static final long serialVersionUID = -5144055068797033748L;

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Long leaveId;

    /**
     * 评论请假单
     */
    @ManyToOne(targetEntity = Post.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    /**
     * 请假内容
     */
//    @NotBlank(message = "请假内容不能为空")
    @Lob
    private String leaveContent;


//    /**
//     * 是否禁用登录
//     */
//    @JsonIgnore
//    private String loginEnable = "true";

    /**
     * 新建日期
     */
    private Date leaveDate;

    /**
     * 最后一次更新时间
     */
    private Date leaveUpdate;

    /**
     * 参与状态 0=未读  1=不参加 2=参加
     */
    private Integer status = 0;

    /**
     * 发表用户 多对一
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "white_id")
    private White white;
}
