package cc.ryanc.halo.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     白名单信息
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Data
@Entity
@Table(name = "halo_white")
public class White implements Serializable {

    private static final long serialVersionUID = -5144055068797033748L;

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Long whiteId;

    /**
     * 姓名
     */
    private String whiteName;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phoneNo;
    /**
     */
    @JsonIgnore
    private Integer sex = 0;
    /**
     * 角色 1=党委 2=党支部
     */
    private Integer roleId = 0;

    /**
     * 是否禁用登录
     */

    private String loginEnable = "true";

    /**
     * 新建日期
     */
    private Date whiteDate;

    /**
     * 最后一次更新时间
     */
    private Date whiteUpdate;


    /**
     * 微信号
     */
    @OneToOne(mappedBy = "white")
    @JsonIgnore
    private WxUser wxUser;

    /**
     * 发表用户 多对一
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Transient
    private String openId;

    @Transient
    private String sessionKey;
}
