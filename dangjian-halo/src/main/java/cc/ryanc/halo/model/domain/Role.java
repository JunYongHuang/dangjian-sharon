package cc.ryanc.halo.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     文章分类
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/30
 */
@Data
@Entity
@Table(name = "halo_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 8383678847517271505L;

    /**
     * 分类编号
     */
    @Id
    @GeneratedValue
    private Long roleId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String roleName;

    /**
     * 分类路径
     */
    @NotBlank(message = "分类路径不能为空")
    private String roleUrl;

    /**
     * 分类描述
     */
    private String roleDesc;


//    /**
//     * 白名单
//     */
//    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
//    private List<White> whites = new ArrayList<>();

}
