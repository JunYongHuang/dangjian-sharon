package cc.ryanc.halo.model.domain;

import lombok.Data;

import javax.persistence.*;
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
@Table(name = "halo_wxUser")
public class WxUser implements Serializable {

    private static final long serialVersionUID = -5144055068797033748L;

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Long wxUserId;

    private String openId;
    private String nickName;
    private String sex;
    private String headUrl;
    private String province;
    private String city;
    private Date createDate;
    private Date lastLoginDate;
    private String ip;//ip

    /**
     * 白名单用户 多对一
     */
    @OneToOne(optional = true)
    @JoinColumn(name = "white_id")
    private White white;
}
