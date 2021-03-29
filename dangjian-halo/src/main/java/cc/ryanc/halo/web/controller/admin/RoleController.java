package cc.ryanc.halo.web.controller.admin;


import cc.ryanc.halo.model.domain.Role;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.ResultCodeEnum;

import cc.ryanc.halo.service.RoleService;
import cc.ryanc.halo.utils.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * <pre>
 *     后台分类管理控制器
 * </pre>
 *
 * @author : HJY
 * @date : 2020/12/10
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     * 查询所有分类并渲染role页面
     *
     * @return 模板路径admin/admin_role
     */
    @GetMapping
    public String rolegories() {
        return "admin/admin_role";
    }

    /**
     * 新增/修改分类目录
     *
     * @param role role对象
     * @return 重定向到/admin/role
     */
    @PostMapping(value = "/save")
    public String saveRole(@ModelAttribute Role role) {
        try {
            roleService.save(role);
        } catch (Exception e) {
            log.error("Modify role failed: {}", e.getMessage());
        }
        return "redirect:/admin/role";
    }

    /**
     * 验证分类目录路径是否已经存在
     *
     * @param roleUrl 分类路径
     * @return JsonResult
     */
    @GetMapping(value = "/checkUrl")
    @ResponseBody
    public JsonResult checkRoleUrlExists(@RequestParam("roleUrl") String roleUrl) {
        Role role = roleService.findByRoleUrl(roleUrl);
        if (null != role) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.url-is-exists"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), "");
    }

    /**
     * 处理删除分类目录的请求
     *
     * @param roleId roleId
     * @return 重定向到/admin/role
     */
    @GetMapping(value = "/remove")
    public String removeRole(@RequestParam("roleId") Long roleId) {
        try {
            roleService.remove(roleId);
        } catch (Exception e) {
            log.error("Delete role failed: {}", e.getMessage());
        }
        return "redirect:/admin/role";
    }

    /**
     * 跳转到修改页面
     *
     * @param roleId roleId
     * @param model  model
     * @return 模板路径admin/admin_role
     */
    @GetMapping(value = "/edit")
    public String toEditRole(Model model, @RequestParam("roleId") Long roleId) {
        Optional<Role> role = roleService.findByRoleId(roleId);
        model.addAttribute("updateRole", role.get());
        return "admin/admin_role";
    }
}
