package cc.ryanc.halo.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

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
@Table(name = "halo_bkCode")
public class BKCode implements Serializable {

    private static final long serialVersionUID = -5144055068797033748L;

    /**
     * 编号
     */
    @Id
    @GeneratedValue
    private Long id;
    private Long phonenum;//手机号
    private String code;//验证码
    private Date createDate;//上传时间
}
