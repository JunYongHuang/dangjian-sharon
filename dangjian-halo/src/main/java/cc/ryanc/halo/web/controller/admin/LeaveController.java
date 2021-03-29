package cc.ryanc.halo.web.controller.admin;

import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.User;
import cc.ryanc.halo.model.domain.Leave;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.dto.LogsRecord;
import cc.ryanc.halo.model.enums.ResultCodeEnum;
import cc.ryanc.halo.service.LogsService;
import cc.ryanc.halo.service.LeaveService;
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
 *     后台请假单管理控制器
 * </pre>
 *
 * @author : HJY
 * @date : 2020/12/10
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/leaves")
public class LeaveController extends BaseController {

    @Autowired
    private LeaveService leaveService;

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
     * 处理后台获取请假单列表的请求
     *
     * @param model model
     * @param page  当前页码
     * @param size  每页显示的条数
     * @return 模板路径admin/admin_leave
     */
    @GetMapping
    public String leaves(Model model,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Leave> leaves = leaveService.findLeave(pageable);
        model.addAttribute("leaves", leaves);
        return "admin/admin_leave";
    }

    /**
     * 模糊查询请假单
     *
     * @param model   Model
     * @param keyword keyword 关键字
     * @param page    page 当前页码
     * @param size    size 每页显示条数
     * @return 模板路径admin/admin_leave
     */
    @PostMapping(value = "/search")
    public String searchLeave(Model model,
                             @RequestParam(value = "keyword") String keyword,
                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            //排序规则
            Sort sort = new Sort(Sort.Direction.DESC, "leaveId");
            Pageable pageable = PageRequest.of(page, size, sort);
            model.addAttribute("leaves", leaveService.searchLeaves(keyword, pageable));
        } catch (Exception e) {
            log.error("未知错误：{}", e.getMessage());
        }
        return "admin/admin_leave";
    }

    /**
     * 处理预览请假单的请求
     *
     * @param leaveId 请假单编号
     * @param model  model
     * @return 模板路径/themes/{theme}/leave
     */
    @GetMapping(value = "/view")
    public String viewLeave(@RequestParam("leaveId") Long leaveId, Model model) {
        Optional<Leave> leave = leaveService.findByLeaveId(leaveId);
        model.addAttribute("leave", leave.orElse(new Leave()));
        return this.render("leave");
    }

    /**
     * 处理跳转到新建请假单页面
     *
     * @return 模板路径admin/admin_editor
     */
    @GetMapping(value = "/write")
    public String writeLeave() {
        return "admin/admin_leave_new";
    }

    /**
     * 跳转到编辑请假单页面
     *
     * @param leaveId 请假单编号
     * @param model  model
     * @return 模板路径admin/admin_editor
     */
    @GetMapping(value = "/edit")
    public String editLeave(@RequestParam("leaveId") Long leaveId, Model model) {
        Optional<Leave> leave = leaveService.findByLeaveId(leaveId);
        model.addAttribute("leave", leave.get());
        return "admin/admin_leave_edit";
    }

    /**
     * 添加请假单
     *
     * @param leave     leave
     * @param session  session
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonResult save(@ModelAttribute Leave leave,
                           @RequestParam("whiteId") Long whiteId,
                           @RequestParam("postId") Long postId,
                           HttpSession session) {
        User user = (User) session.getAttribute(HaloConst.USER_SESSION_KEY);
        Optional<Post> post = postService.findByPostId(postId);
        Optional<White> white = whiteService.findByWhiteId(whiteId);
        try {
            leave.setPost(post.get());
            leave.setWhite(white.get());

            leave.setLeaveDate(DateUtil.date());
            leave.setLeaveUpdate(DateUtil.date());
//            leave.setUser(user);

            leaveService.save(leave);
            logsService.save(LogsRecord.PUSH_LEAVE, leave.getWhite().getWhiteName(), request);
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
     * @param leave     leave
     * @return JsonResult
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public JsonResult update(@ModelAttribute Leave leave,
                           @RequestParam("whiteId") Long whiteId,
                             @RequestParam("postId") Long postId) {
        //old data
        Leave oldLeave = leaveService.findByLeaveId(leave.getLeaveId()).orElse(new Leave());
        leave.setLeaveUpdate(new Date());
        leave.setPost(oldLeave.getPost());
        leave.setWhite(oldLeave.getWhite());
        if (null == leave.getLeaveDate()) {
            leave.setLeaveDate(new Date());
        }

        leave = leaveService.save(leave);
        if (null != leave) {
            return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.update-success"));
        } else {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.update-failed"));
        }
    }



    /**
     * 处理删除请假单的请求
     *
     * @param leaveId 请假单编号
     * @return 重定向到/admin/leaves
     */
    @GetMapping(value = "/remove")
    public String removeLeave(@RequestParam("leaveId") Long leaveId) {
        try {
            Optional<Leave> leave = leaveService.findByLeaveId(leaveId);
            leaveService.remove(leaveId);
            logsService.save(LogsRecord.REMOVE_LEAVE, leave.get().getWhite().getWhiteName(), request);
        } catch (Exception e) {
            log.error("Delete article failed: {}", e.getMessage());
        }

            return "redirect:/admin/leaves";

    }





    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
}
