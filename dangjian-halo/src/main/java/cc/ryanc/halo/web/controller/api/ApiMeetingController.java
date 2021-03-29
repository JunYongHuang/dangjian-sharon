package cc.ryanc.halo.web.controller.api;

import cc.ryanc.halo.model.domain.*;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.PostTypeEnum;
import cc.ryanc.halo.model.enums.ResponseStatusEnum;
import cc.ryanc.halo.service.LeaveService;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.service.SpecialTypeService;
import cc.ryanc.halo.service.WxUserService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
@RequestMapping(value = "/api/meetings")
public class ApiMeetingController {

    @Autowired
    private SpecialTypeService specialTypeService;

    @Autowired
    private PostService postService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private LeaveService leaveService;

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
    public JsonResult meetings() {
        List<JSONObject> list=new ArrayList<>();
        List<SpecialType> meetings = specialTypeService.findAll();
        for(SpecialType i:meetings){
            JSONObject info=new JSONObject();
            info.put("specialTypeId",i.getSpecialTypeId());
            info.put("count",i.getPosts().size());
            info.put("specialTypeName",i.getSpecialTypeName());
            info.put("specialTypeUrl",i.getSpecialTypeUrl());
            info.put("meetings",i.getSpecialTypeDesc());
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
    public JsonResult meetings(@PathVariable("specialTypeUrl") String specialTypeUrl) {
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
    @RequestMapping("/page/{page}")
    public JsonResult meetings(Model model,
                                 @PathVariable("page") Integer page,
                               @RequestBody String body) {

        org.json.JSONObject jo = new org.json.JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Sort sort = new Sort(Sort.Direction.DESC, "postDate");
            Integer size = 10;
            if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
                size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
            }
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Post> posts = postService.findByRoleIdAndStatus(white.getRoleId(),0, PostTypeEnum.POST_TYPE_MEETING.getDesc(), pageable);
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(),posts);

        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }

    }

    /**
     * 根据分类目录查询所有文章 分页
     * @param  body String
     * @return String
     */
    @RequestMapping("/attendMeeting")
    public JsonResult attendMeeting(Model model,
                               @RequestBody String body) {

        org.json.JSONObject jo = new org.json.JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Long postId = jo.getLong("postId");

            Post post = postService.findByPostId(postId, PostTypeEnum.POST_TYPE_MEETING.getDesc());
            if(post!=null){
                Integer status = jo.getInt("status");
                String leaveContent = jo.getString("leaveContent");
                Leave leave = leaveService.findByWhiteAndPost(white, post);
                if(leave == null)
                {
                    leave = new Leave();
                    leave.setWhite(white);
                    leave.setPost(post);
                    leave.setLeaveDate(DateUtil.date());
                }
                leave.setStatus(status);
                leave.setLeaveContent(leaveContent);
                leave.setLeaveUpdate(DateUtil.date());
                leaveService.save(leave);
                return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(),leave);

            }else{
                String errorStr = "会议通知不存在";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }


        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }

    }


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




}
