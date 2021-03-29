package cc.ryanc.halo.web.controller.admin;

import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.model.domain.User;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.dto.LogsRecord;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.ResultCodeEnum;
import cc.ryanc.halo.service.LogsService;
import cc.ryanc.halo.service.WhiteService;
import cc.ryanc.halo.utils.HaloUtils;
import cc.ryanc.halo.utils.LocaleMessageUtil;
import cc.ryanc.halo.utils.MarkdownUtils;
import cc.ryanc.halo.web.controller.core.BaseController;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
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
@RequestMapping(value = "/admin/whites")
public class WhiteController extends BaseController {

    @Autowired
    private WhiteService whiteService;

    @Autowired
    private LogsService logsService;

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
     * @return 模板路径admin/admin_white
     */
    @GetMapping
    public String whites(Model model,
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<White> whites = whiteService.findWhite(pageable);
        model.addAttribute("whites", whites);
        return "admin/admin_white";
    }

    /**
     * 模糊查询文章
     *
     * @param model   Model
     * @param keyword keyword 关键字
     * @param page    page 当前页码
     * @param size    size 每页显示条数
     * @return 模板路径admin/admin_white
     */
    @PostMapping(value = "/search")
    public String searchWhite(Model model,
                             @RequestParam(value = "keyword") String keyword,
                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            //排序规则
            Sort sort = new Sort(Sort.Direction.DESC, "whiteId");
            Pageable pageable = PageRequest.of(page, size, sort);
            model.addAttribute("whites", whiteService.searchWhites(keyword, pageable));
        } catch (Exception e) {
            log.error("未知错误：{}", e.getMessage());
        }
        return "admin/admin_white";
    }

    /**
     * 处理预览文章的请求
     *
     * @param whiteId 文章编号
     * @param model  model
     * @return 模板路径/themes/{theme}/white
     */
    @GetMapping(value = "/view")
    public String viewWhite(@RequestParam("whiteId") Long whiteId, Model model) {
        Optional<White> white = whiteService.findByWhiteId(whiteId);
        model.addAttribute("white", white.orElse(new White()));
        return this.render("white");
    }

    /**
     * 处理跳转到新建文章页面
     *
     * @return 模板路径admin/admin_editor
     */
    @GetMapping(value = "/write")
    public String writeWhite() {
        return "admin/admin_white_new";
    }

    /**
     * 跳转到编辑文章页面
     *
     * @param whiteId 文章编号
     * @param model  model
     * @return 模板路径admin/admin_editor
     */
    @GetMapping(value = "/edit")
    public String editWhite(@RequestParam("whiteId") Long whiteId, Model model) {
        Optional<White> white = whiteService.findByWhiteId(whiteId);
        model.addAttribute("white", white.get());
        return "admin/admin_white_edit";
    }

    /**
     * 添加文章
     *
     * @param white     white
     * @param session  session
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonResult save(@ModelAttribute White white,
                           HttpSession session) {
        User user = (User) session.getAttribute(HaloConst.USER_SESSION_KEY);
        try {

            white.setWhiteDate(DateUtil.date());
            white.setWhiteUpdate(DateUtil.date());
            white.setUser(user);

            whiteService.save(white);
            logsService.save(LogsRecord.PUSH_WHITE, white.getWhiteName(), request);
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
     * @param white     white
     * @return JsonResult
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public JsonResult update(@ModelAttribute White white) {
        //old data
        White oldWhite = whiteService.findByWhiteId(white.getWhiteId()).orElse(new White());
        white.setWhiteUpdate(new Date());

        white.setUser(oldWhite.getUser());
        if (null == white.getWhiteDate()) {
            white.setWhiteDate(new Date());
        }

        white = whiteService.save(white);
        if (null != white) {
            return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.update-success"));
        } else {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.update-failed"));
        }
    }



    /**
     * 处理删除文章的请求
     *
     * @param whiteId 文章编号
     * @return 重定向到/admin/whites
     */
    @GetMapping(value = "/remove")
    public String removeWhite(@RequestParam("whiteId") Long whiteId) {
        try {
            Optional<White> white = whiteService.findByWhiteId(whiteId);
            whiteService.remove(whiteId);
            logsService.save(LogsRecord.REMOVE_WHITE, white.get().getWhiteName(), request);
        } catch (Exception e) {
            log.error("Delete article failed: {}", e.getMessage());
        }

            return "redirect:/admin/whites";

    }





    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
}
