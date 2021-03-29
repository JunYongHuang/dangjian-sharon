package cc.ryanc.halo.web.controller.api;

import cc.ryanc.halo.model.domain.*;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.PostStatusEnum;
import cc.ryanc.halo.model.enums.PostTypeEnum;
import cc.ryanc.halo.model.enums.ResponseStatusEnum;
import cc.ryanc.halo.service.BacklogService;
import cc.ryanc.halo.service.LeaveService;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.service.WxUserService;
import cn.hutool.core.util.StrUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     文章API
 * </pre>
 *
 * @author : HJY
 * @date : 2018/6/6
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/posts")
public class ApiPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private BacklogService backlogService;
    @Autowired
    private LeaveService leaveService;

    /**
     * 获取文章列表 分页
     *
     * <p>
     *     result api
     *     <pre>
     * {
     *     "code": 200,
     *     "msg": "OK",
     *     "result": {
     *         "content": [
     *             {
     *                 "postId": ,
     *                 "user": {},
     *                 "postTitle": "",
     *                 "postType": "",
     *                 "postContentMd": "",
     *                 "postContent": "",
     *                 "postUrl": "",
     *                 "postSummary": "",
     *                 "categories": [],
     *                 "tags": [],
     *                 "comments": [],
     *                 "postThumbnail": "",
     *                 "postDate": "",
     *                 "postUpdate": "",
     *                 "postStatus": 0,
     *                 "postViews": 0,
     *                 "allowComment": 1,
     *                 "customTpl": ""
     *             }
     *         ],
     *         "pageable": {
     *             "sort": {
     *                 "sorted": true,
     *                 "unsorted": false,
     *                 "empty": false
     *             },
     *             "offset": 0,
     *             "pageSize": 10,
     *             "pageNumber": 0,
     *             "unpaged": false,
     *             "paged": true
     *         },
     *         "last": true,
     *         "totalElements": 1,
     *         "totalPages": 1,
     *         "size": 10,
     *         "number": 0,
     *         "first": true,
     *         "numberOfElements": 1,
     *         "sort": {
     *             "sorted": true,
     *             "unsorted": false,
     *             "empty": false
     *         },
     *         "empty": false
     *     }
     * }
     *     </pre>
     * </p>
     *
     * @param page 页码
     * @return JsonResult
     */
    @GetMapping(value = "/page/{page}")
    public JsonResult posts(@PathVariable(value = "page") Integer page) {
        Sort sort = new Sort(Sort.Direction.DESC, "postDate");
        int size = 10;
        if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
            size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
        }
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Post> posts = postService.findPostByStatus(PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_SPECIAL.getDesc(), pageable);
        if (null == posts) {
            return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
        }
        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), posts);
    }

    /**
     * 搜索文章
     * @param page 页码
     * @param keyword keyword
     */
    @GetMapping(value = "search")
    public JsonResult search(@RequestParam("keyword") String keyword, @PathVariable(value = "page") Integer page) {
        Sort sort = new Sort(Sort.Direction.DESC, "postDate");
        int size = 10;
        if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
            size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
        }
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Post> posts = postService.searchByKeywords(keyword, pageable);
        if (null == posts) {
            return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
        }
        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), posts);
    }
    /**
     * 获取单个文章信息
     *
     * <p>
     *     result json:
     *     <pre>
     * {
     *     "code": 200,
     *     "msg": "OK",
     *     "result": {
     *         "postId": ,
     *         "user": {},
     *         "postTitle": "",
     *         "postType": "",
     *         "postContentMd": "",
     *         "postContent": "",
     *         "postUrl": "",
     *         "postSummary": "",
     *         "categories": [],
     *         "tags": [],
     *         "comments": [],
     *         "postThumbnail": "",
     *         "postDate": "",
     *         "postUpdate": "",
     *         "postStatus": 0,
     *         "postViews": 0,
     *         "allowComment": 1,
     *         "customTpl": ""
     *     }
     * }
     *     </pre>
     * </p>
     *
     * @param postId 文章编号
     * @return JsonResult
     */
    @RequestMapping(value = "/{postId}")
    public JsonResult posts(@PathVariable(value = "postId") Long postId,
                            @RequestBody String body) {

        JSONObject jo = new JSONObject(body);

        


        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Optional<Post> post = postService.findByPostId(postId);
            if (null != post) {
                Post _post = post.get();
                boolean hasRight = hasRight(_post, white);
                Backlog backlog = backlogService.findByWhiteAndPost(white, _post);
                if(hasRight){

                    if(_post.getPostStatus().equals(PostStatusEnum.PUBLISHED.getCode())){

                        postService.cacheViews(post.get().getPostId());
                        if(backlog != null && backlog.getBacklogStatus() < 1){
                            backlog.setBacklogStatus(1);
                            backlogService.save(backlog);//没有读过标记为已读
                        }
                        if(_post.getPostType().equals("meeting")){
                            Leave myLeave = leaveService.findByWhiteAndPost(white, _post);
                            if(myLeave!=null){
                                _post.setMyLeave(myLeave);
                            }

                        }
                        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), _post);
                    }else{

                        if(backlog!=null){
                            backlogService.remove(backlog.getBacklogId());
                        }
                        String errorStr = "没有发布";
                        return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                    }


                }else{

                    if(backlog!=null){
                        backlogService.remove(backlog.getBacklogId());
                    }
                    String errorStr = "没有权限查看";
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                }


            } else {
                String errorStr = "未找到指定内容";
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }

        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }
    }
/**
     * 获取单个专题信息
     * @param postId 文章编号
     * @return JsonResult
     */
    @RequestMapping(value = "/mySpecials/{postId}")
    public JsonResult mySpecials(@PathVariable(value = "postId") Long postId,
                            @RequestBody String body) {

        JSONObject jo = new JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Optional<Post> post = postService.findByPostId(postId);
            if (null != post) {
                Post _post = post.get();
                boolean hasRight = hasRight(_post, white);

                if(hasRight){
                    return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), _post);

                }else{

                    String errorStr = "没有权限查看";
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                }


            } else {
                String errorStr = "未找到指定内容";
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }

        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }
    }

    /**
     * 文章读过5秒以上
     * @param postId 文章编号
     * @return JsonResult
     */
    @RequestMapping(value = "/updateRead/{postId}")
    public JsonResult updateRead(@PathVariable(value = "postId") Long postId,
                            @RequestBody String body) {

        JSONObject jo = new JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Optional<Post> post = postService.findByPostId(postId);
            if (null != post) {
                Post _post = post.get();
                boolean hasRight = hasRight(_post, white);
                if(hasRight){
                    Backlog backlog = backlogService.findByWhiteAndPost(white, _post);
                    if(backlog != null && backlog.getBacklogStatus() < 2){
                        backlog.setBacklogStatus(2);
                        backlogService.save(backlog);//没有读过标记为已读5秒以上
                    }
                    return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), "已读5秒以上");
                }else{
                    String errorStr = "没有权限查看";
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                }


            } else {
                String errorStr = "未找到指定内容";
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }

        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }
    }

    private boolean hasRight(Post _post, White white) {
        if(_post.getPostType() == PostTypeEnum.POST_TYPE_POST.getDesc() || _post.getPostType() == PostTypeEnum.POST_TYPE_MEETING.getDesc()){
            List<Category> categories = _post.getCategories();
            for (Category category : categories) {
                if(category.getRoleId().equals(white.getRoleId())){
                    return true;
                }
            }
            return false;

        }else{
            return true;
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
