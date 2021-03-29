package cc.ryanc.halo.web.controller.api;

import cc.ryanc.halo.model.domain.BKCode;
import cc.ryanc.halo.model.domain.Backlog;
import cc.ryanc.halo.model.domain.White;
import cc.ryanc.halo.model.domain.WxUser;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.ResponseStatusEnum;
import cc.ryanc.halo.service.BKCodeService;
import cc.ryanc.halo.service.BacklogService;
import cc.ryanc.halo.service.WhiteService;
import cc.ryanc.halo.service.WxUserService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.dyw.utils.WxConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * <pre>
 *     文章分类API
 * </pre>
 *
 * @author : HJY
 * @date : 2018/6/6
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/api/backlogs")
public class ApiBacklogController {

    @Autowired
    private BacklogService backlogService;

    @Autowired
    private WxUserService wxUserService;



    private White isWhiteEnable(Long whiteId, String openId){
        if (whiteId>0 && !StringUtils.isEmpty(openId)) {

            WxUser wxUser = wxUserService.findByOpenId(openId);
            if(wxUser != null){
                White white =  wxUser.getWhite();
                if(white != null && white.getLoginEnable().equals("true") && white.getWhiteId().equals(whiteId)){
                    return white;
                }
            }
        }
        return null;
    }

    /**
     * 根据分类目录查询所有文章 分页
     * @param page    页码
     * @return String
     */
    @RequestMapping("/page/{page}")
    public JsonResult meetings(Model model,
                               @PathVariable("page") Integer page,
                               @RequestBody String body) {


        JSONObject jo = new JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Sort sort = new Sort(Sort.Direction.DESC, "backlogDate");
            Integer size = 10;
            if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
                size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
            }
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            int backlogStatus = 2;
            Page<Backlog> backlogs = backlogService.findByWhiteAndLessThanBacklogStatus(white, pageable, backlogStatus);
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(),backlogs);
        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }
        

        

    }
}
