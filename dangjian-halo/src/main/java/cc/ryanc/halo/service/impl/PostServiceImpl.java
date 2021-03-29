package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.*;
import cc.ryanc.halo.model.dto.Archive;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.dto.LogsRecord;
import cc.ryanc.halo.model.enums.PostStatusEnum;
import cc.ryanc.halo.model.enums.PostTypeEnum;
import cc.ryanc.halo.model.enums.ResultCodeEnum;
import cc.ryanc.halo.repository.PostRepository;
import cc.ryanc.halo.service.*;
import cc.ryanc.halo.utils.HaloUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static cc.ryanc.halo.model.enums.AttachLocationEnum.*;

/**
 * <pre>
 *     文章业务逻辑实现类
 * </pre>
 *
 * @author : HJY
 * @date : 2020/11/14
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private static final String POSTS_CACHE_NAME = "posts";

    private static final String COMMENTS_CACHE_NAME = "comments";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private SpecialTypeService specialTypeService;

    @Autowired
    private BacklogService backlogService;
    @Autowired
    private AttachmentService attachmentService;
    /**
     * 保存文章
     *
     * @param post Post
     * @param cateList
     * @return Post
     */
    @Override
    @CacheEvict(value = {POSTS_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Post save(Post post, List<String> cateList) {
        Post _post = postRepository.save(post);
        if(cateList!=null && _post.getPostStatus().equals(PostStatusEnum.PUBLISHED.getCode())){//如果是已经发布
            backlogService.sendBacklog(_post, cateList);
        }

        return _post;
    }

    /**
     * 根据编号移除文章
     *
     * @param postId postId
     * @return Post
     */
    @Override
    @CacheEvict(value = {POSTS_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Post remove(Long postId) {
        Optional<Post> post = this.findByPostId(postId);
        postRepository.delete(post.get());
        return post.get();
    }

    /**
     * 修改文章状态
     *
     * @param postId postId
     * @param status status
     * @return Post
     */
    @Override
    @CacheEvict(value = POSTS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public Post updatePostStatus(Long postId, Integer status) {
        Optional<Post> post = this.findByPostId(postId);
        post.get().setPostStatus(status);
        return postRepository.save(post.get());
    }

    /**
     * 批量更新文章摘要
     *
     * @param postSummary postSummary
     */
    @Override
    @CacheEvict(value = POSTS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void updateAllSummary(Integer postSummary) {
        List<Post> posts = this.findAll(PostTypeEnum.POST_TYPE_POST.getDesc());
        for (Post post : posts) {
            String text = StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(post.getPostContent()));
            if (text.length() > postSummary) {
                post.setPostSummary(text.substring(0, postSummary));
            } else {
                post.setPostSummary(text);
            }
            postRepository.save(post);
        }
    }

    /**
     * 获取文章列表 不分页
     *
     * @param postType post or page
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_type_'+#postType")
    public List<Post> findAll(String postType) {
        return postRepository.findPostsByPostType(postType);
    }

    /**
     * 模糊查询文章
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    @Override
    public List<Post> searchPosts(String keyWord, Pageable pageable) {
        return postRepository.findByPostTitleLike(keyWord, pageable);
    }

    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param status   0，1，2
     * @param postType post or page
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<Post> findPostByStatus(Integer status, String postType, Pageable pageable) {
        return postRepository.findPostsByPostStatusAndPostType(status, postType, pageable);
    }

    /**
     * 根据文章状态查询 分页，首页分页
     *
     * @param pageable pageable
     * @return Page
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_page_'+#pageable.pageNumber")
    public Page<Post> findPostByStatus(Pageable pageable) {
        return postRepository.findPostsByPostStatusAndPostType(PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getDesc(), pageable);
    }

    /**
     * 根据文章状态查询
     *
     * @param status   0，1，2
     * @param postType post or page
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_status_type_'+#status+'_'+#postType")
    public List<Post> findPostByStatus(Integer status, String postType) {
        return postRepository.findPostsByPostStatusAndPostType(status, postType);
    }

    /**
     * 根据编号查询文章
     *
     * @param postId postId
     * @return Optional
     */
    @Override
    public Optional<Post> findByPostId(Long postId) {
        return postRepository.findById(postId);
    }

    /**
     * 根据编号和类型查询文章
     *
     * @param postId postId
     * @return Post
     */
    @Override
    public Post findByPostId(Long postId, String postType) {
        return postRepository.findPostByPostIdAndPostType(postId, postType);
    }

    /**
     * 根据文章路径查询
     *
     * @param postUrl  路径
     * @param postType post or page
     * @return Post
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_posturl_'+#postUrl+'_'+#postType")
    public Post findByPostUrl(String postUrl, String postType) {
        return postRepository.findPostByPostUrlAndPostType(postUrl, postType);
    }

    /**
     * 查询最新的5篇文章
     *
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_latest_'+#postType")
    public List<Post> findPostLatest(String postType) {
        return postRepository.findTopFive(postType);
    }

    /**
     * 查询之后的文章
     *
     * @param postDate 发布时间
     * @return List
     */
    @Override
    public List<Post> findByPostDateAfter(Date postDate) {
        return postRepository.findByPostDateAfterAndPostStatusAndPostTypeOrderByPostDateDesc(postDate, PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getDesc());
    }

    /**
     * 查询Id之前的文章
     *
     * @param postDate 发布时间
     * @return List
     */
    @Override
    public List<Post> findByPostDateBefore(Date postDate) {
        return postRepository.findByPostDateBeforeAndPostStatusAndPostTypeOrderByPostDateAsc(postDate, PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getDesc());
    }


    /**
     * 查询归档信息 根据年份和月份
     *
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'archives_year_month'")
    public List<Archive> findPostGroupByYearAndMonth() {
        List<Object[]> objects = postRepository.findPostGroupByYearAndMonth();
        List<Archive> archives = new ArrayList<>();
        Archive archive = null;
        for (Object[] obj : objects) {
            archive = new Archive();
            archive.setYear(obj[0].toString());
            archive.setMonth(obj[1].toString());
            archive.setCount(obj[2].toString());
            archive.setPosts(this.findPostByYearAndMonth(obj[0].toString(), obj[1].toString()));
            archives.add(archive);
        }
        return archives;
    }

    /**
     * 查询归档信息 根据年份
     *
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'archives_year_'+#postType")
    public List<Archive> findPostGroupByYear(String postType) {
        List<Object[]> objects = postRepository.findPostGroupByYear(postType);
        List<Archive> archives = new ArrayList<>();
        Archive archive = null;
        for (Object[] obj : objects) {
            archive = new Archive();
            archive.setYear(obj[0].toString());
            archive.setCount(obj[1].toString());
            archive.setPosts(this.findPostByYear(obj[0].toString(), postType));
            archives.add(archive);
        }
        return archives;
    }

    /**
     * 根据年份和月份查询文章
     *
     * @param year  year
     * @param month month
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_year_month_'+#year+'_'+#month")
    public List<Post> findPostByYearAndMonth(String year, String month) {
        return postRepository.findPostByYearAndMonth(year, month);
    }

    /**
     * 根据年份查询文章
     *
     * @param year year
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_year_'+#year +'_'+#postType")
    public List<Post> findPostByYear(String year, String postType) {
        return postRepository.findPostByYear(year, postType);
    }

    /**
     * 根据年份和月份索引文章
     *
     * @param year     year year
     * @param month    month month
     * @param pageable pageable pageable
     * @return Page
     */
    @Override
    public Page<Post> findPostByYearAndMonth(String year, String month, Pageable pageable) {
        return postRepository.findPostByYearAndMonth(year, month, null);
    }

    /**
     * 根据分类目录查询文章
     *
     * @param category category
     * @param status   status
     * @param pageable pageable
     * @return Page
     */
    @Override
    @CachePut(value = POSTS_CACHE_NAME, key = "'posts_category_'+#category.cateId+'_'+#postType+'_'+#pageable.pageNumber")
    public Page<Post> findPostByCategoriesAndPostType(Category category, String postType, Pageable pageable) {
        return postRepository.findPostByCategoriesAndPostStatusAndPostType(category, PostStatusEnum.PUBLISHED.getCode(), postType, pageable);
    }

    /**
     * 根据分类目录查询文章
     *
     * @param specialType specialType
     * @param status   status
     * @param pageable pageable
     * @return Page
     */
    @Override
    @CachePut(value = POSTS_CACHE_NAME, key = "'posts_specialType_'+#specialType.specialTypeId+'_'+#pageable.pageNumber")
    public Page<Post> findPostBySpecialType(SpecialType specialType, Pageable pageable) {
        return postRepository.findPostBySpecialTypeAndPostStatus(specialType, PostStatusEnum.PUBLISHED.getCode(), pageable);
    }

    @Override
    public Page<Post> findByRoleIdAndStatus(Integer roleId, int status, String postType, Pageable pageable) {
        return postRepository.findByRoleIdAndStatus(roleId, status, postType, pageable);
    }

    @Override
    public Page<Post> findPostByWhite(White white, Pageable pageable) {
        return postRepository.findByWhite(white, pageable);
    }


    /**
     * 根据标签查询文章，分页
     *
     * @param tag      tag
//     * @param status   status
     * @param pageable pageable
     * @return Page
     */
    @Override
    @CachePut(value = POSTS_CACHE_NAME, key = "'posts_tag_'+#tag.tagId+'_'+#pageable.pageNumber")
    public Page<Post> findPostsByTags(Tag tag, Pageable pageable) {
        return postRepository.findPostsByTagsAndPostStatus(tag, PostStatusEnum.PUBLISHED.getCode(), pageable);
    }

    /**
     * 搜索文章
     *
     * @param keyword  关键词
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<Post> searchByKeywords(String keyword, Pageable pageable) {
        return postRepository.findPostByPostTitleLikeOrPostContentLikeAndPostTypeAndPostStatus(keyword, pageable);
    }

    /**
     * 热门文章
     *
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_hot'")
    public List<Post> hotPosts() {
        return postRepository.findPostsByPostTypeOrderByPostViewsDesc(PostTypeEnum.POST_TYPE_POST.getDesc());
    }

    /**
     * 当前文章的相似文章
     *
     * @param post post
     * @return List
     */
    @Override
    @CachePut(value = POSTS_CACHE_NAME, key = "'posts_related_'+#post.getPostId()")
    public List<Post> relatedPosts(Post post) {
        //获取当前文章的所有标签
        List<Tag> tags = post.getTags();
        List<Post> tempPosts = new ArrayList<>();
        for (Tag tag : tags) {
            tempPosts.addAll(postRepository.findPostsByTags(tag));
        }
        //去掉当前的文章
        tempPosts.remove(post);
        //去掉重复的文章
        List<Post> allPosts = new ArrayList<>();
        for (int i = 0; i < tempPosts.size(); i++) {
            if (!allPosts.contains(tempPosts.get(i))) {
                allPosts.add(tempPosts.get(i));
            }
        }
        return allPosts;
    }

    /**
     * 获取所有文章的阅读量
     *
     * @return Long
     */
    @Override
    public Long getPostViews() {
        return postRepository.getPostViewsSum();
    }

    /**
     * 根据文章状态查询数量
     *
     * @param status 文章状态
     * @return 文章数量
     */
    @Override
    public Integer getCountByStatus(Integer status, String postType) {
        return postRepository.countAllByPostStatusAndPostType(status, postType);
    }

    /**
     * 生成rss
     *
     * @param posts posts
     * @return String
     */
    @Override
    public String buildRss(List<Post> posts) {
        String rss = "";
        try {
            rss = HaloUtils.getRss(posts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rss;
    }

    /**
     * 生成sitemap
     *
     * @param posts posts
     * @return String
     */
    @Override
    public String buildSiteMap(List<Post> posts) {
        return HaloUtils.getSiteMap(posts);
    }

    /**
     * 缓存阅读数
     *
     * @param postId postId
     */
    @Override
    public void cacheViews(Long postId) {
//        if (null != HaloConst.POSTS_VIEWS.get(postId)) {
//            HaloConst.POSTS_VIEWS.put(postId, HaloConst.POSTS_VIEWS.get(postId) + 1);
//        } else {
//            HaloConst.POSTS_VIEWS.put(postId, 1L);
//        }
        //更新阅读数
        Post p= postRepository.getOne(postId);
        p.setPostViews(p.getPostViews()+1);
        postRepository.saveAndFlush(p);

    }

    /**
     * 组装分类目录和标签
     *
     * @param post     post
     * @param cateList cateList
     * @param tagList  tagList
     * @return Post Post
     */
    @Override
    public Post buildCategoriesAndTags(Post post, List<String> cateList, String tagList) {
        List<Category> categories = categoryService.strListToCateList(cateList);
        post.setCategories(categories);
        if (StrUtil.isNotEmpty(tagList)) {
            List<Tag> tags = tagService.strListToTagList(StrUtil.trim(tagList));
            post.setTags(tags);
        }
        return post;
    }

    /**
     * 组装分类专题
     *
     * @param post     post
     * @param Long specialTypeId
     * @return Post Post
     */
    @Override
    public Post buildSpecialTypes(Post post, Long specialTypeId) {
        Optional<SpecialType> specialType = specialTypeService.findBySpecialTypeId(specialTypeId);

        if (specialType!=null) {

            post.setSpecialType(specialType.get());
        }
        return post;
    }


    @Override
    public void removeAttachment(Attachment attachment) {
        
        String attachLocation = attachment.getAttachLocation();
        String delFileName = attachment.getAttachName();
        boolean flag = true;
        try {
            //删除数据库中的内容
            attachmentService.remove(attachment.getAttachId());
            if (attachLocation != null) {
                if (attachLocation.equals(SERVER.getDesc())) {
                    String delSmallFileName = delFileName.substring(0, delFileName.lastIndexOf('.')) + "_small." + attachment.getAttachSuffix();
                    //删除文件
                    String userPath = System.getProperties().getProperty("user.home") + "/halo";
                    File mediaPath = new File(userPath, attachment.getAttachPath().substring(0, attachment.getAttachPath().lastIndexOf('/')));
                    File delFile = new File(new StringBuffer(mediaPath.getAbsolutePath()).append("/").append(delFileName).toString());
                    File delSmallFile = new File(new StringBuffer(mediaPath.getAbsolutePath()).append("/").append(delSmallFileName).toString());
                    if (delFile.exists() && delFile.isFile()) {
                        flag = delFile.delete() && delSmallFile.delete();
                    }
                } else if (attachLocation.equals(QINIU.getDesc())) {
                    //七牛删除
                    String attachPath = attachment.getAttachPath();
                    String key = attachPath.substring(attachPath.lastIndexOf("/") + 1);
                    flag = attachmentService.deleteQiNiuAttachment(key);
                } else if (attachLocation.equals(UPYUN.getDesc())) {
                    //又拍删除
                    String attachPath = attachment.getAttachPath();
                    String fileName = attachPath.substring(attachPath.lastIndexOf("/") + 1);
                    flag = attachmentService.deleteUpYunAttachment(fileName);
                } else {
                    //..
                }
            }
            if (flag) {
                log.info("Delete file {} successfully!", delFileName);
//                logsService.save(LogsRecord.REMOVE_FILE, delFileName, request);
            } else {
                log.error("Deleting attachment {} failed!", delFileName);
//                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-failed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Deleting attachment {} failed: {}", delFileName, e.getMessage());
//            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-failed"));
        }
//        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
    }

    @Override
    public List<Post> findAllByPostTypeAndPostStatus(String postType, Integer postStatus) {
        return postRepository.findAllByPostTypeAndPostStatus(postType, postStatus);
    }
}
