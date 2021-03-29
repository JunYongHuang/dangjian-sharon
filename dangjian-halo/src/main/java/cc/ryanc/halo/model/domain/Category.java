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
@Table(name = "halo_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 8383678847517271505L;

    /**
     * 分类编号
     */
    @Id
    @GeneratedValue
    private Long cateId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String cateName;

    /**
     * 分类路径
     */
    @NotBlank(message = "分类路径不能为空")
    private String cateUrl;

    /**
     * 分类描述
     */
    private String cateDesc;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    /**
     * 角色 1=党委 2=党支部
     */
    @JsonIgnore
    private Integer roleId = 0;
}
