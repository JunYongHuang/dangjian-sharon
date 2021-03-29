package cc.ryanc.halo.web.controller.admin;


import cc.ryanc.halo.model.domain.SpecialType;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.ResultCodeEnum;
import cc.ryanc.halo.service.SpecialTypeService;
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
@RequestMapping(value = "/admin/specialType")
public class SpecialTypeController {

    @Autowired
    private SpecialTypeService specialTypeService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     * 查询所有分类并渲染specialType页面
     *
     * @return 模板路径admin/admin_specialType
     */
    @GetMapping
    public String specialTypegories() {
        return "admin/admin_specialType";
    }

    /**
     * 新增/修改分类目录
     *
     * @param specialType specialType对象
     * @return 重定向到/admin/specialType
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonResult saveSpecialType(@ModelAttribute SpecialType specialType) {
        try {
            specialTypeService.save(specialType);
            return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
        } catch (Exception e) {
            log.error("Modify specialType failed: {}", e.getMessage());
            log.error("Save article failed: {}", e.getMessage());
            e.printStackTrace();
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.save-failed"));

        }
    }

    /**
     * 验证分类目录路径是否已经存在
     *
     * @param specialTypeUrl 分类路径
     * @return JsonResult
     */
    @GetMapping(value = "/checkUrl")
    @ResponseBody
    public JsonResult checkSpecialTypeUrlExists(@RequestParam("specialTypeUrl") String specialTypeUrl) {
        SpecialType specialType = specialTypeService.findBySpecialTypeUrl(specialTypeUrl);
        if (null != specialType) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.url-is-exists"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), "");
    }

    /**
     * 处理删除分类目录的请求
     *
     * @param specialTypeId specialTypeId
     * @return 重定向到/admin/specialType
     */
    @GetMapping(value = "/remove")
    public String removeSpecialType(@RequestParam("specialTypeId") Long specialTypeId) {
        try {
            specialTypeService.remove(specialTypeId);
        } catch (Exception e) {
            log.error("Delete specialType failed: {}", e.getMessage());
        }
        return "redirect:/admin/specialType";
    }

    /**
     * 跳转到修改页面
     *
     * @param specialTypeId specialTypeId
     * @param model  model
     * @return 模板路径admin/admin_specialType
     */
    @GetMapping(value = "/edit")
    public String toEditSpecialType(Model model, @RequestParam("specialTypeId") Long specialTypeId) {
        Optional<SpecialType> specialType = specialTypeService.findBySpecialTypeId(specialTypeId);
        model.addAttribute("updateSpecialType", specialType.get());
        return "admin/admin_specialType";
    }
}
