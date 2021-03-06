package cc.ryanc.halo.web.controller.core;

import cc.ryanc.halo.model.domain.*;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.LogsRecord;
import cc.ryanc.halo.model.enums.AllowCommentEnum;
import cc.ryanc.halo.model.enums.AttachLocationEnum;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.TrueFalseEnum;
import cc.ryanc.halo.service.*;
import cc.ryanc.halo.utils.MarkdownUtils;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     应用初始化控制器
 * </pre>
 *
 * @author : HJY
 * @date : 2018/1/28
 */
@Slf4j
@Controller
@RequestMapping(value = "/install")
public class InstallController {

    @Autowired
    private OptionsService optionsService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogsService logsService;

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private Configuration configuration;

    /**
     * 渲染安装页面
     *
     * @param model model
     * @return 模板路径
     */
    @GetMapping
    public String install(Model model) {
        try {
            if (StrUtil.equals(TrueFalseEnum.TRUE.getDesc(), HaloConst.OPTIONS.get(BlogPropertiesEnum.IS_INSTALL.getProp()))) {
                model.addAttribute("isInstall", true);
            } else {
                model.addAttribute("isInstall", false);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "common/install";
    }

    /**
     * 执行安装
     *
     * @param blogLocale      系统语言
     * @param blogTitle       应用标题
     * @param blogUrl         应用网址
     * @param userName        用户名
     * @param userDisplayName 用户名显示名
     * @param userEmail       用户邮箱
     * @param userPwd         用户密码
     * @param request         request
     * @return true：安装成功，false：安装失败
     */
    @PostMapping(value = "/do")
    @ResponseBody
    public boolean doInstall(@RequestParam("blogLocale") String blogLocale,
                             @RequestParam("blogTitle") String blogTitle,
                             @RequestParam("blogUrl") String blogUrl,
                             @RequestParam("userName") String userName,
                             @RequestParam("userDisplayName") String userDisplayName,
                             @RequestParam("userEmail") String userEmail,
                             @RequestParam("userPwd") String userPwd,
                             HttpServletRequest request) {
        try {
            if (StrUtil.equals(TrueFalseEnum.TRUE.getDesc(), HaloConst.OPTIONS.get(BlogPropertiesEnum.IS_INSTALL.getProp()))) {
                return false;
            }
            //创建新的用户
            User user = new User();
            user.setUserName(userName);
            if (StrUtil.isBlank(userDisplayName)) {
                userDisplayName = userName;
            }
            user.setUserDisplayName(userDisplayName);
            user.setUserEmail(userEmail);
            user.setUserPass(SecureUtil.md5(userPwd));
            userService.save(user);


            //创建新的用户
            User user1 = new User();
            user1.setUserName("sduser");
            user1.setUserDisplayName("党建中心");
            user1.setUserEmail("i@ryanc.cc");
            user1.setUserPass(SecureUtil.md5("sduserryanc"));
            userService.save(user1);

            //默认分类
            Category category = new Category();
            category.setCateName("未分类");
            category.setCateUrl("default");
            category.setCateDesc("未分类");
            categoryService.save(category);

            Category category1 = new Category();
            category1.setCateName("党委");
            category1.setCateUrl("committee");
            category1.setCateDesc("当写提示单选择党委，具有党委权限的人能看到");
            category1.setRoleId(1);
            categoryService.save(category1);

            Category category2 = new Category();
            category2.setCateName("党支部");
            category2.setCateUrl("branch");
            category2.setCateDesc("当写提示单选择党支部，具有党支部权限的人能看到");
            category2.setRoleId(2);
            categoryService.save(category2);

            //第一篇文章
            Post post = new Post();
            List<Category> categories = new ArrayList<>();
            categories.add(category);
            post.setPostTitle("你好 上党小程序!");
            post.setPostContentMd("# 你好 上党小程序!\n" +
                    "欢迎使用上党小程序管理系统，删除这篇文章后赶紧开始吧。");
            post.setPostContent(MarkdownUtils.renderMarkdown(post.getPostContentMd()));
            post.setPostSummary("欢迎使用上党小程序管理系统，删除这篇文章后赶紧开始吧。");
            post.setPostStatus(0);
            post.setPostDate(DateUtil.date());
            post.setPostUrl("hello-sd");
            post.setUser(user);
            post.setCategories(categories);
            post.setAllowComment(AllowCommentEnum.ALLOW.getCode());
            postService.save(post, null);

            //第一个评论
            Comment comment = new Comment();
            comment.setPost(post);
            comment.setCommentAuthor("上党");
            comment.setCommentAuthorEmail("i@ryanc.cc");
            comment.setCommentAuthorUrl("https://ryanc.cc");
            comment.setCommentAuthorIp("127.0.0.1");
            comment.setCommentAuthorAvatarMd5("7cc7f29278071bd4dce995612d428834");
            comment.setCommentDate(DateUtil.date());
            comment.setCommentContent("欢迎，欢迎！");
            comment.setCommentStatus(0);
            comment.setCommentAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.162 Safari/537.36");
            comment.setIsAdmin(0);
            commentService.save(comment);

            Map<String, String> options = new HashMap<>();
            options.put(BlogPropertiesEnum.IS_INSTALL.getProp(), TrueFalseEnum.TRUE.getDesc());
            options.put(BlogPropertiesEnum.BLOG_LOCALE.getProp(), blogLocale);
            options.put(BlogPropertiesEnum.BLOG_TITLE.getProp(), blogTitle);
            options.put(BlogPropertiesEnum.BLOG_URL.getProp(), blogUrl);
            options.put(BlogPropertiesEnum.THEME.getProp(), "anatole");
            options.put(BlogPropertiesEnum.BLOG_START.getProp(), DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
            options.put(BlogPropertiesEnum.SMTP_EMAIL_ENABLE.getProp(), TrueFalseEnum.FALSE.getDesc());
            options.put(BlogPropertiesEnum.NEW_COMMENT_NOTICE.getProp(), TrueFalseEnum.FALSE.getDesc());
            options.put(BlogPropertiesEnum.COMMENT_PASS_NOTICE.getProp(), TrueFalseEnum.FALSE.getDesc());
            options.put(BlogPropertiesEnum.COMMENT_REPLY_NOTICE.getProp(), TrueFalseEnum.FALSE.getDesc());
            options.put(BlogPropertiesEnum.ATTACH_LOC.getProp(), AttachLocationEnum.SERVER.getDesc());
            optionsService.saveOptions(options);

            //更新日志
            logsService.save(LogsRecord.INSTALL, "安装成功，欢迎使用Halo。", request);

            Menu menuIndex = new Menu();
            menuIndex.setMenuName("首页");
            menuIndex.setMenuUrl("/");
            menuIndex.setMenuSort(1);
            menuIndex.setMenuIcon("");
            menuService.save(menuIndex);

            Menu menuArchive = new Menu();
            menuArchive.setMenuName("归档");
            menuArchive.setMenuUrl("/archives");
            menuArchive.setMenuSort(2);
            menuArchive.setMenuIcon("");
            menuService.save(menuArchive);

            Menu menuSpecial = new Menu();
            menuSpecial.setMenuName("专题");
            menuSpecial.setMenuUrl("/specials");
            menuSpecial.setMenuSort(3);
            menuSpecial.setMenuIcon("");
            menuService.save(menuSpecial);

            Menu menuMeeting = new Menu();
            menuMeeting.setMenuName("会议通知");
            menuMeeting.setMenuUrl("/meetings");
            menuMeeting.setMenuSort(4);
            menuMeeting.setMenuIcon("");
            menuService.save(menuMeeting);

            HaloConst.OPTIONS.clear();
            HaloConst.OPTIONS = optionsService.findAllOptions();
            configuration.setSharedVariable("options", HaloConst.OPTIONS);
            configuration.setSharedVariable("user", userService.findUser());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
}
