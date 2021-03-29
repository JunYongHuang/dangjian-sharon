package cc.ryanc.halo.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     文章／页面
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Data
@Entity
@Table(name = "halo_post")
public class Post implements Serializable {

    private static final long serialVersionUID = -6019684584665869629L;

    /**
     * 文章编号
     */
    @Id
    @GeneratedValue
    private Long postId;

    /**
     * 发表用户 多对一
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 文章标题
     */
    private String postTitle;

    /**
     * 文章类型
     * post  文章
     * page  页面
     * special 专题
     * meeting 会议
     */
    private String postType = "post";

    /**
     * 文章内容 Markdown格式
     */
    @Lob
    private String postContentMd;

    /**
     * 文章内容 html格式
     */
    @Lob
    private String postContent;

    /**
     * 文章路径
     */
    @Column(unique = true)
    private String postUrl;

    /**
     * 文章摘要
     */
    private String postSummary;

    /**
     * 文章所属分类
     */
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "halo_posts_categories",
            joinColumns = {@JoinColumn(name = "post_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "cate_id", nullable = false)})
    @JsonIgnore
    private List<Category> categories = new ArrayList<>();

    /**
     * 文章所属标签
     */
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "halo_posts_tags",
            joinColumns = {@JoinColumn(name = "post_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", nullable = false)})
    @JsonIgnore
    private List<Tag> tags = new ArrayList<>();

    /**
     * 文章的评论
     */
    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    /**
     * 文章的待办
     */
    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Backlog> backlogs = new ArrayList<>();


    /**
     * 会议的参加信息
     */
    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Leave> leaves = new ArrayList<>();

    @Transient
    private Leave myLeave;

    /**
     * 缩略图
     */
    private String postThumbnail;

    /**
     * 发表日期
     */
    private Date postDate;

    /**
     * 最后一次更新时间
     */
    private Date postUpdate;

    /**
     * 提示单、会议| 专题
     * 0 已发布    已发布
     * 1 草稿      待审核
     * 2 回收站    回收站
     */
    private Integer postStatus = 0;

    /**
     * 文章访问量
     */
    private Long postViews = 0L;

    /**
     * 是否允许评论
     */
    private Integer allowComment = 0;



    /**
     * 审核不通过原因
     */
    @Lob
    private String examineContent;

    /**
     * 指定渲染模板
     */
    private String customTpl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getPostDate() {
        return postDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getPostUpdate() {
        return postUpdate;
    }



    /**
     * 所属白名单id
     */
    @ManyToOne()
    @JoinColumn(name = "white_id")
    private White white;

    /**
     * 专题类型
     */
    @ManyToOne()
    @JoinColumn(name = "specialType_id")
    private SpecialType specialType;

}
