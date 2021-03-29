package cc.ryanc.halo.web.controller.admin;

import cc.ryanc.halo.model.domain.Attachment;
import cc.ryanc.halo.model.domain.Comment;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.User;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.dto.LogsRecord;
import cc.ryanc.halo.model.enums.*;
import cc.ryanc.halo.service.AttachmentService;
import cc.ryanc.halo.service.LogsService;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.utils.HaloUtils;
import cc.ryanc.halo.utils.LocaleMessageUtil;
import cc.ryanc.halo.utils.MarkdownUtils;
import cc.ryanc.halo.utils.OwoUtil;
import cc.ryanc.halo.web.controller.core.BaseController;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     后台文章管理控制器
 * </pre>
 *
 * @author : HJY
 * @date : 2020/12/10
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/specials")
public class SpecialController extends BaseController {

    @Autowired
    private PostService postService;

    @Autowired
    private LogsService logsService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    @Autowired
    private AttachmentService attachmentService;
    /**
     * 去除html，htm后缀，以及将空格替换成-
     *
     * @param url url
     * @return String
     */
    private static String urlFilter(String url) {
        if (null != url) {
            final boolean urlEndsWithHtmlPostFix = url.endsWith(".html") || url.endsWith(".htm");
            if (urlEndsWithHtmlPostFix) {
                return url.substring(0, url.lastIndexOf("."));
            }
        }
        return StrUtil.replace(url, " ", "-");
    }

    /**
     * 处理后台获取文章列表的请求
     *
     * @param model model
     * @param page  当前页码
     * @param size  每页显示的条数
     * @return 模板路径admin/admin_special
     */
    @GetMapping
    public String posts(Model model,
                        @RequestParam(value = "status", defaultValue = "0") Integer status,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "postDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postService.findPostByStatus(status, PostTypeEnum.POST_TYPE_SPECIAL.getDesc(), pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("publishCount", postService.getCountByStatus(PostStatusEnum.PUBLISHED.getCode(),PostTypeEnum.POST_TYPE_SPECIAL.getDesc()));
        model.addAttribute("draftCount", postService.getCountByStatus(PostStatusEnum.DRAFT.getCode(),PostTypeEnum.POST_TYPE_SPECIAL.getDesc()));
        model.addAttribute("trashCount", postService.getCountByStatus(PostStatusEnum.RECYCLE.getCode(),PostTypeEnum.POST_TYPE_SPECIAL.getDesc()));
        model.addAttribute("status", status);
        return "admin/admin_special";
    }

    /**
     * 模糊查询文章
     *
     * @param model   Model
     * @param keyword keyword 关键字
     * @param page    page 当前页码
     * @param size    size 每页显示条数
     * @return 模板路径admin/admin_special
     */
    @PostMapping(value = "/search")
    public String searchPost(Model model,
                             @RequestParam(value = "keyword") String keyword,
                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            //排序规则
            Sort sort = new Sort(Sort.Direction.DESC, "postId");
            Pageable pageable = PageRequest.of(page, size, sort);
            model.addAttribute("posts", postService.searchPosts(keyword, pageable));
        } catch (Exception e) {
            log.error("未知错误：{}", e.getMessage());
        }
        return "admin/admin_special";
    }

    /**
     * 处理预览文章的请求
     *
     * @param postId 文章编号
     * @param model  model
     * @return 模板路径/themes/{theme}/special
     */
    @GetMapping(value = "/view")
    public String viewPost(@RequestParam("postId") Long postId, Model model) {
        Optional<Post> post = postService.findByPostId(postId);
        model.addAttribute("post", post.orElse(new Post()));
        return this.render("post");
    }

    /**
     * 处理跳转到新建文章页面
     *
     * @return 模板路径admin/admin_editor
     */
    @GetMapping(value = "/write")
    public String writePost() {
        return "admin/admin_special_new";
    }

    /**
     * 跳转到编辑文章页面
     *
     * @param postId 文章编号
     * @param model  model
     * @return 模板路径admin/admin_editor
     */
    @GetMapping(value = "/edit")
    public String editPost(@RequestParam("postId") Long postId, Model model) {
        Optional<Post> post = postService.findByPostId(postId);
        model.addAttribute("post", post.get());
        return "admin/admin_special_edit";
    }

    /**
     * 添加文章
     *
     * @param post     post
     * @param session  session
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonResult save(@ModelAttribute Post post,
                           @RequestParam("specialTypeId") Long specialTypeId,
                           HttpSession session) {
        User user = (User) session.getAttribute(HaloConst.USER_SESSION_KEY);
        try {
            post.setPostContent(MarkdownUtils.renderMarkdown(post.getPostContentMd()));
            //摘要字数
            int postSummary = 80;
            if (StrUtil.isNotEmpty(HaloConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()))) {
                postSummary = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()));
            }
            //设置文章摘要
            String summaryText = StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(post.getPostContent()));
            if (summaryText.length() > postSummary) {
                String summary = summaryText.substring(0, postSummary);
                post.setPostSummary(summary);
            } else {
                post.setPostSummary(summaryText);
            }
            post.setPostType(PostTypeEnum.POST_TYPE_SPECIAL.getDesc());
            post.setPostDate(DateUtil.date());
            post.setPostUpdate(DateUtil.date());
            post.setUser(user);
            post = postService.buildSpecialTypes(post, specialTypeId);

//            post = postService.buildCategoriesAndTags(post, cateList, tagList);
            post.setPostUrl(urlFilter(post.getPostUrl()));
            //当没有选择文章缩略图的时候，自动分配一张内置的缩略图
            if (StrUtil.equals(post.getPostThumbnail(), BlogPropertiesEnum.DEFAULT_THUMBNAIL.getProp())) {
                post.setPostThumbnail("/static/halo-frontend/images/thumbnail/thumbnail-" + RandomUtil.randomInt(1, 10) + ".jpg");
            }
            postService.save(post, null);
            logsService.save(LogsRecord.PUSH_SPECIAL, post.getPostTitle(), request);
            return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
        } catch (Exception e) {
            log.error("Save article failed: {}", e.getMessage());
            e.printStackTrace();
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.save-failed"));
        }
    }

    /**
     * 更新
     *
     * @param post     post
     * @return JsonResult
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public JsonResult update(@ModelAttribute Post post,
                             @RequestParam("specialTypeId") Long specialTypeId) {
        //old data
        Post oldPost = postService.findByPostId(post.getPostId()).orElse(new Post());
        post.setPostUpdate(new Date());
        post.setPostViews(oldPost.getPostViews());
        post.setPostContent(MarkdownUtils.renderMarkdown(post.getPostContentMd()));
        post.setUser(oldPost.getUser());
        if (null == post.getPostDate()) {
            post.setPostDate(new Date());
        }
        //摘要字数
        int postSummary = 50;
        if (StrUtil.isNotEmpty(HaloConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()))) {
            postSummary = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()));
        }
        //设置文章摘要
        String summaryText = StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(post.getPostContent()));
        if (summaryText.length() > postSummary) {
            String summary = summaryText.substring(0, postSummary);
            post.setPostSummary(summary);
        } else {
            post.setPostSummary(summaryText);
        }
        post.setPostType(PostTypeEnum.POST_TYPE_SPECIAL.getDesc());
        post = postService.buildSpecialTypes(post, specialTypeId);
        //当没有选择文章缩略图的时候，自动分配一张内置的缩略图
        if (StrUtil.equals(post.getPostThumbnail(), BlogPropertiesEnum.DEFAULT_THUMBNAIL.getProp())) {
            post.setPostThumbnail("/static/halo-frontend/images/thumbnail/thumbnail-" + RandomUtil.randomInt(1, 10) + ".jpg");
        }

        post = postService.save(post, null);
        if (null != post) {
            return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.update-success"));
        } else {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.update-failed"));
        }
    }

    /**
     * 处理移至回收站的请求
     *
     * @param postId 文章编号
     * @return 重定向到/admin/specials
     */
    @GetMapping(value = "/throw")
    public String moveToTrash(@RequestParam("postId") Long postId, @RequestParam("status") Integer status) {
        try {
            postService.updatePostStatus(postId, PostStatusEnum.RECYCLE.getCode());
            log.info("Article number {} has been moved to the recycle bin", postId);
        } catch (Exception e) {
            log.error("Deleting article to recycle bin failed: {}", e.getMessage());
        }
        return "redirect:/admin/specials?status=" + status;
    }

    /**
     * 处理文章为发布的状态
     *
     * @param postId 文章编号
     * @return 重定向到/admin/specials
     */
    @GetMapping(value = "/revert")
    public String moveToPublish(@RequestParam("postId") Long postId,
                                @RequestParam("status") Integer status) {
        try {
            if(status.equals(PostStatusEnum.RECYCLE.getCode()))
                postService.updatePostStatus(postId, PostStatusEnum.DRAFT.getCode());
            else
                postService.updatePostStatus(postId, PostStatusEnum.PUBLISHED.getCode());
            log.info("Article number {} has been changed to release status", postId);
        } catch (Exception e) {
            log.error("Publishing article failed: {}", e.getMessage());
        }
        return "redirect:/admin/specials?status=" + status;
    }

    /**
     * 处理删除文章的请求
     *
     * @param postId 文章编号
     * @return 重定向到/admin/specials
     */
    @GetMapping(value = "/remove")
    public String removePost(@RequestParam("postId") Long postId, @RequestParam("postType") String postType) {
        try {
            Optional<Post> post = postService.findByPostId(postId);
            List<Attachment> attachments = attachmentService.findByPostId(postId);
            //删除照片
            for (Attachment attachment : attachments) {
                postService.removeAttachment(attachment);
            }
            postService.remove(postId);
            logsService.save(LogsRecord.REMOVE_SPECIAL, post.get().getPostTitle(), request);
        } catch (Exception e) {
            log.error("Delete article failed: {}", e.getMessage());
        }
        if (StrUtil.equals(PostTypeEnum.POST_TYPE_SPECIAL.getDesc(), postType)) {
            return "redirect:/admin/specials?status=2";
        }
        return "redirect:/admin/page";
    }

    /**
     * 更新所有摘要
     *
     * @param postSummary 文章摘要字数
     * @return JsonResult
     */
    @GetMapping(value = "/updateSummary")
    @ResponseBody
    public JsonResult updateSummary(@RequestParam("postSummary") Integer postSummary) {
        try {
            postService.updateAllSummary(postSummary);
        } catch (Exception e) {
            log.error("Update summary failed: {}", e.getMessage());
            e.printStackTrace();
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.update-failed"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.update-success"));
    }

    /**
     * 验证文章路径是否已经存在
     *
     * @param postUrl 文章路径
     * @return JsonResult
     */
    @GetMapping(value = "/checkUrl")
    @ResponseBody
    public JsonResult checkUrlExists(@RequestParam("postUrl") String postUrl) {
        postUrl = urlFilter(postUrl);
        Post post = postService.findByPostUrl(postUrl, PostTypeEnum.POST_TYPE_SPECIAL.getDesc());
        if (null != post) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.url-is-exists"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), "");
    }

    /**
     * 将所有文章推送到百度
     *
     * @param baiduToken baiduToken
     * @return JsonResult
     */
    @GetMapping(value = "/pushAllToBaidu")
    @ResponseBody
    public JsonResult pushAllToBaidu(@RequestParam("baiduToken") String baiduToken) {
        if (StrUtil.isBlank(baiduToken)) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.post.no-baidu-token"));
        }
        String blogUrl = HaloConst.OPTIONS.get(BlogPropertiesEnum.BLOG_URL.getProp());
        List<Post> posts = postService.findAll(PostTypeEnum.POST_TYPE_SPECIAL.getDesc());
        StringBuilder urls = new StringBuilder();
        for (Post post : posts) {
            urls.append(blogUrl);
            urls.append("/specials/");
            urls.append(post.getPostUrl());
            urls.append("\n");
        }
        String result = HaloUtils.baiduPost(blogUrl, baiduToken, urls.toString());
        if (StrUtil.isEmpty(result)) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.post.push-to-baidu-failed"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.post.push-to-baidu-success"));
    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }






    /**
     * 管理员回复专题不通过理由
     *
     * @param commentContent 回复的内容
     * @return JsonResult
     */
    @PostMapping(value = "/reply")
    @ResponseBody
    public JsonResult replyComment(@RequestParam("postId") Long postId,
                                   @RequestParam("commentContent") String commentContent,
                                   @RequestParam("userAgent") String userAgent,
                                   HttpServletRequest request,
                                   HttpSession session) {
        try {
            Optional<Post> _post = postService.findByPostId(postId);
            if(_post!=null){
                Post post = _post.get();
                post.setPostStatus(PostStatusEnum.RECYCLE.getCode());
                post.setExamineContent(commentContent);
                postService.save(post, null);

                //            //邮件通知
//            new CommentController.EmailToAuthor(comment, lastComment, post, user, commentContent).start();
                return new JsonResult(ResultCodeEnum.SUCCESS.getCode());
            }else{
                log.error("Reply to comment failed: {}", "专题不存在");
                return new JsonResult(ResultCodeEnum.FAIL.getCode());
            }



        } catch (Exception e) {
            log.error("Reply to comment failed: {}", e.getMessage());
            return new JsonResult(ResultCodeEnum.FAIL.getCode());
        }
    }

}
