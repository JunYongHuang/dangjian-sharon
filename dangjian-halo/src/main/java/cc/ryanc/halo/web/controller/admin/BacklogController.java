package cc.ryanc.halo.web.controller.admin;

import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.User;
import cc.ryanc.halo.model.domain.Backlog;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.dto.LogsRecord;
import cc.ryanc.halo.model.enums.ResultCodeEnum;
import cc.ryanc.halo.service.LogsService;
import cc.ryanc.halo.service.BacklogService;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.service.WhiteService;
import cc.ryanc.halo.utils.LocaleMessageUtil;
import cc.ryanc.halo.web.controller.core.BaseController;
import cn.hutool.core.date.DateUtil;
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
@RequestMapping(value = "/admin/backlogs")
public class BacklogController extends BaseController {

    @Autowired
    private BacklogService backlogService;

    @Autowired
    private LogsService logsService;

    @Autowired
    private PostService postService;


    @Autowired
    private WhiteService whiteService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;



    /**
     * 处理后台获取文章列表的请求
     *
     * @param model model
     * @param page  当前页码
     * @param size  每页显示的条数
     * @return 模板路径admin/admin_backlog
     */
    @GetMapping
    public String backlogs(Model model,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Backlog> backlogs = backlogService.findBacklogByRight(pageable);
        model.addAttribute("backlogs", backlogs);
        return "admin/admin_backlog";
    }

    /**
     * 模糊查询文章
     *
     * @param model   Model
     * @param keyword keyword 关键字
     * @param page    page 当前页码
     * @param size    size 每页显示条数
     * @return 模板路径admin/admin_backlog
     */
    @PostMapping(value = "/search")
    public String searchBacklog(Model model,
                             @RequestParam(value = "keyword") String keyword,
                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            //排序规则
            Sort sort = new Sort(Sort.Direction.DESC, "backlogId");
            Pageable pageable = PageRequest.of(page, size, sort);
            model.addAttribute("backlogs", backlogService.searchBacklogs(keyword, pageable));
        } catch (Exception e) {
            log.error("未知错误：{}", e.getMessage());
        }
        return "admin/admin_backlog";
    }

    /**
     * 处理预览文章的请求
     *
     * @param backlogId 文章编号
     * @param model  model
     * @return 模板路径/themes/{theme}/backlog
     */
    @GetMapping(value = "/view")
    public String viewBacklog(@RequestParam("backlogId") Long backlogId, Model model) {
        Optional<Backlog> backlog = backlogService.findByBacklogId(backlogId);
        model.addAttribute("backlog", backlog.orElse(new Backlog()));
        return this.render("backlog");
    }

    /**
     * 处理跳转到新建文章页面
     *
     * @return 模板路径admin/admin_editor
     */
    @GetMapping(value = "/write")
    public String writeBacklog() {
        return "admin/admin_backlog_new";
    }

    /**
     * 跳转到编辑文章页面
     *
     * @param backlogId 文章编号
     * @param model  model
     * @return 模板路径admin/admin_editor
     */
    @GetMapping(value = "/edit")
    public String editBacklog(@RequestParam("backlogId") Long backlogId, Model model) {
        Optional<Backlog> backlog = backlogService.findByBacklogId(backlogId);
        model.addAttribute("backlog", backlog.get());
        return "admin/admin_backlog_edit";
    }

    /**
     * 添加文章
     *
     * @param backlog     backlog
     * @param session  session
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonResult save(@ModelAttribute Backlog backlog,
                           @RequestParam("whiteId") Long whiteId,
                           @RequestParam("postId") Long postId,
                           HttpSession session) {
        User user = (User) session.getAttribute(HaloConst.USER_SESSION_KEY);
        try {
            Optional<Post> post = postService.findByPostId(postId);
            Optional<White> white = whiteService.findByWhiteId(whiteId);

            backlog.setPost(post.get());
            backlog.setWhite(white.get());
            backlog.setBacklogDate(DateUtil.date());
            backlog.setBacklogUpdate(DateUtil.date());
//            backlog.setUser(user);

            backlogService.save(backlog);
            logsService.save(LogsRecord.PUSH_BACKLOG, backlog.getBacklogName(), request);
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
     * @param backlog     backlog
     * @return JsonResult
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public JsonResult update(@ModelAttribute Backlog backlog,
                             @RequestParam("whiteId") Long whiteId,
                             @RequestParam("postId") Long postId) {
        //old data
        Backlog oldBacklog = backlogService.findByBacklogId(backlog.getBacklogId()).orElse(new Backlog());
        backlog.setBacklogUpdate(new Date());
        backlog.setPost(oldBacklog.getPost());
        backlog.setWhite(oldBacklog.getWhite());
//        backlog.setUser(oldBacklog.getUser());
        if (null == backlog.getBacklogDate()) {
            backlog.setBacklogDate(new Date());
        }

        backlog = backlogService.save(backlog);
        if (null != backlog) {
            return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.update-success"));
        } else {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.update-failed"));
        }
    }



    /**
     * 处理删除文章的请求
     *
     * @param backlogId 文章编号
     * @return 重定向到/admin/backlogs
     */
    @GetMapping(value = "/remove")
    public String removeBacklog(@RequestParam("backlogId") Long backlogId) {
        try {
            Optional<Backlog> backlog = backlogService.findByBacklogId(backlogId);
            backlogService.remove(backlogId);
            logsService.save(LogsRecord.REMOVE_BACKLOG, backlog.get().getBacklogName(), request);
        } catch (Exception e) {
            log.error("Delete article failed: {}", e.getMessage());
        }

            return "redirect:/admin/backlogs";

    }





    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
}
