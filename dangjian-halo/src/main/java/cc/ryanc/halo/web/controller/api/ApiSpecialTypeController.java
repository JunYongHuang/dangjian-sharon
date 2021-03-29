package cc.ryanc.halo.web.controller.api;

import cc.ryanc.halo.model.domain.SpecialType;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.ResponseStatusEnum;
import cc.ryanc.halo.service.SpecialTypeService;
import cc.ryanc.halo.service.PostService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     文章分类API
 * </pre>
 *
 * @author : HJY
 * @date : 2018/6/6
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/specialtypes")
public class ApiSpecialTypeController {

    @Autowired
    private SpecialTypeService specialTypeService;




    /**
     * 获取所有分类
     *
     * <p>
     *     result json:
     *     <pre>
     * {
     *     "code": 200,
     *     "msg": "OK",
     *     "result": [
     *         {
     *             "specialTypeId": "",
     *             "specialTypeName": "",
     *             "specialTypeUrl": "",
     *             "specialTypeDesc": ""
     *             "count":"文章总数"
     *         }
     *     ]
     * }
     *     </pre>
     * </p>
     *
     * @return JsonResult
     */
    @GetMapping
    public JsonResult specialtypes() {
        List<JSONObject> list=new ArrayList<>();
        List<SpecialType> specialtypes = specialTypeService.findAll();
        for(SpecialType i:specialtypes){
            JSONObject info=new JSONObject();
            info.put("specialTypeId",i.getSpecialTypeId());
            info.put("count",i.getPosts().size());
            info.put("specialTypeName",i.getSpecialTypeName());
            info.put("specialTypeUrl",i.getSpecialTypeUrl());
            info.put("specialtypes",i.getSpecialTypeDesc());
            info.put("postThumbnail",i.getPostThumbnail());
            list.add(info);

        }
        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), list);



    }

    /**
     * 获取单个分类的信息
     *
     * <p>
     *     result json:
     *     <pre>
     * {
     *     "code": 200,
     *     "msg": "OK",
     *     "result": {
     *         "specialTypeId": "",
     *         "specialTypeName": "",
     *         "specialTypeUrl": "",
     *         "specialTypeDesc": ""
     *     }
     * }
     *     </pre>
     * </p>
     *
     * @param specialTypeUrl 分类路径
     * @return JsonResult
     */
    @GetMapping(value = "/{specialTypeUrl}")
    public JsonResult specialtypes(@PathVariable("specialTypeUrl") String specialTypeUrl) {
        SpecialType specialType = specialTypeService.findBySpecialTypeUrl(specialTypeUrl);
        if (null != specialType) {
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), specialType);
        } else {
            return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
        }
    }

    /**
     * 根据分类目录查询所有文章 分页
     * @param page    页码
     * @return String
     */
    @GetMapping("/page/{page}")
    public JsonResult specialtypes(Model model,
                                 @PathVariable("page") Integer page) {

            Sort sort = new Sort(Sort.Direction.DESC, "postDate");
            Integer size = 10;
            if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
                size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<SpecialType> specialTypes = specialTypeService.findSpecialType(pageable);
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(),specialTypes);

    }
}
