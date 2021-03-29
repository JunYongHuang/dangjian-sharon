package cc.ryanc.halo.web.controller.api;

import cc.ryanc.halo.model.domain.*;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.dto.LogsRecord;
import cc.ryanc.halo.model.enums.*;
import cc.ryanc.halo.service.AttachmentService;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.service.SpecialTypeService;
import cc.ryanc.halo.service.WxUserService;
import cc.ryanc.halo.utils.MarkdownUtils;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONObject;
import com.dyw.utils.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


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
@RequestMapping(value = "/api/specials")
public class ApiSpecialController {

    @Autowired
    private SpecialTypeService specialTypeService;

    @Autowired
    private PostService postService;

    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private AttachmentService attachmentService;

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
    public JsonResult specials() {
        List<JSONObject> list=new ArrayList<>();
        List<SpecialType> specials = specialTypeService.findAll();
        for(SpecialType i:specials){
            JSONObject info=new JSONObject();
            info.put("specialTypeId",i.getSpecialTypeId());
            info.put("count",i.getPosts().size());
            info.put("specialTypeName",i.getSpecialTypeName());
            info.put("specialTypeUrl",i.getSpecialTypeUrl());
            info.put("specials",i.getSpecialTypeDesc());
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
    public JsonResult specials(@PathVariable("specialTypeUrl") String specialTypeUrl) {
        SpecialType specialType = specialTypeService.findBySpecialTypeUrl(specialTypeUrl);
        if (null != specialType) {
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), specialType);
        } else {
            return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
        }
    }

    /**
     * 根据分类目录查询所有文章 分页
     * @param specialTypeUrl 分类目录路径
     * @param page    页码
     * @return String
     */
    @GetMapping("{specialTypeUrl}/page/{page}")
    public JsonResult specials(Model model,
                                 @PathVariable("specialTypeUrl") String specialTypeUrl,
                                 @PathVariable("page") Integer page) {
        Optional<SpecialType> specialType = specialTypeService.findBySpecialTypeId(Long.parseLong(specialTypeUrl));
        if (null != specialType) {
            Sort sort = new Sort(Sort.Direction.DESC, "postDate");
            Integer size = 10;
            if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
                size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
            }
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Post> posts = postService.findPostBySpecialType(specialType.get(), pageable);
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(),posts);
        }else{
            return new JsonResult(ResponseStatusEnum.NOTFOUND.getCode(), ResponseStatusEnum.NOTFOUND.getMsg());
        }
    }



    /**
     * 添加专题
     * @param  body String
     * @return String
     */
    @RequestMapping("/addSpecial")
    public JsonResult addSpecial(Model model,
                                 @RequestBody String body) {

        org.json.JSONObject jo = new org.json.JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Long specialTypeId = jo.getLong("specialTypeId");
            String postTitle = jo.getString("postTitle");
            String postContent = jo.getString("postContent");



            try {
                Post post = new Post();
                post.setPostTitle(postTitle);
                post.setPostContent(postContent);


                //摘要字数
                int postSummary = 80;
                if (StrUtil.isNotEmpty(HaloConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()))) {
                    postSummary = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()));
                }
                //设置文章摘要
                String summaryText = StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(post.getPostContent()));
                if (summaryText.length() > postSummary) {
                    String summary = summaryText.substring(0, postSummary);
                    post.setPostSummary(summary);
                } else {
                    post.setPostSummary(summaryText);
                }
                post.setPostType(PostTypeEnum.POST_TYPE_SPECIAL.getDesc());
                post.setPostDate(DateUtil.date());
                post.setPostUpdate(DateUtil.date());
//                post.setUser(user);
                post.setWhite(white);
                post = postService.buildSpecialTypes(post, specialTypeId);
                post.setPostStatus(PostStatusEnum.DRAFT.getCode());//设置待审核状态
//            post = postService.buildCategoriesAndTags(post, cateList, tagList);
                post.setPostUrl(getPostUrl());
                //当没有选择文章缩略图的时候，自动分配一张内置的缩略图
                if (StrUtil.equals(post.getPostThumbnail(), BlogPropertiesEnum.DEFAULT_THUMBNAIL.getProp())) {
                    post.setPostThumbnail("/static/halo-frontend/images/thumbnail/thumbnail-" + RandomUtil.randomInt(1, 10) + ".jpg");
                }
                Post p = postService.save(post, null);

                //==============删除多余照片 begin==============

                List<Attachment> attachments = attachmentService.findByWhiteIdAndPostIdOrZero(whiteId, p.getPostId());

                Map lists = ToolUtil.getImgStr(postContent, false);

                //删除照片
                for (Attachment attachment : attachments) {
                    String path = attachment.getAttachPath();
//                    Boolean b = (Boolean) lists.get(path);
                    if(lists.get(path) != null){
                        attachment.setPostId(post.getPostId());
                        attachmentService.save(attachment);
                    }else{
                        postService.removeAttachment(attachment);
                    }

                }
//                logsService.save(LogsRecord.PUSH_POST, post.getPostTitle(), request);
                return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), p.getPostId());

            } catch (Exception e) {
                log.error("Save article failed: " + e.getMessage());
                e.printStackTrace();
                String errorStr = "保存失败";
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }


        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }

    }




    /**
     * 添加专题
     * @param  body String
     * @return String
     */
    @RequestMapping("/updateSpecial")
    public JsonResult updateSpecial(Model model,
                                    @RequestBody String body) {

        org.json.JSONObject jo = new org.json.JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Long postId = jo.getLong("postId");
            String postTitle = jo.getString("postTitle");
            String postContent = jo.getString("postContent");



            try {
                Optional<Post> _post = postService.findByPostId(postId);
                if(_post!=null){
                    Post post = _post.get();
                    if(post.getWhite().getWhiteId().equals(whiteId)){
                        post.setPostTitle(postTitle);
                        post.setPostContent(postContent);
                        //摘要字数
                        int postSummary = 80;
                        if (StrUtil.isNotEmpty(HaloConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()))) {
                            postSummary = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.POST_SUMMARY.getProp()));
                        }
                        //设置文章摘要
                        String summaryText = StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(post.getPostContent()));
                        if (summaryText.length() > postSummary) {
                            String summary = summaryText.substring(0, postSummary);
                            post.setPostSummary(summary);
                        } else {
                            post.setPostSummary(summaryText);
                        }
                        post.setPostStatus(PostStatusEnum.DRAFT.getCode());//设置待审核状态
                        post.setPostType(PostTypeEnum.POST_TYPE_SPECIAL.getDesc());
//                        post.setPostDate(DateUtil.date());
                        post.setPostUpdate(DateUtil.date());
//                post.setUser(user);
//                        post.setWhite(white);
//                        post = postService.buildSpecialTypes(post, specialTypeId);

//            post = postService.buildCategoriesAndTags(post, cateList, tagList);
//                        post.setPostUrl(getPostUrl());
                        //当没有选择文章缩略图的时候，自动分配一张内置的缩略图
//                        if (StrUtil.equals(post.getPostThumbnail(), BlogPropertiesEnum.DEFAULT_THUMBNAIL.getProp())) {
//                            post.setPostThumbnail("/static/halo-frontend/images/thumbnail/thumbnail-" + RandomUtil.randomInt(1, 10) + ".jpg");
//                        }
                        Post p = postService.save(post, null);

                        //==============删除多余照片 begin==============

                        List<Attachment> attachments = attachmentService.findByWhiteIdAndPostIdOrZero(whiteId, p.getPostId());

                        Map lists = ToolUtil.getImgStr(postContent, false);

                        //删除照片
                        for (Attachment attachment : attachments) {
                            String path = attachment.getAttachPath();
//                            Boolean b = (Boolean) lists.get(path);
                            if(lists.get(path) != null){
                                attachment.setPostId(post.getPostId());
                                attachmentService.save(attachment);
                            }else{
                                postService.removeAttachment(attachment);
                            }

                        }
//                logsService.save(LogsRecord.PUSH_POST, post.getPostTitle(), request);
                        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), p.getPostId());

                    }else {
                        String errorStr = "不是本人不能操作";
                        return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                    }

                }else{
                    String errorStr = "专题不存在";
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);

                }


            } catch (Exception e) {
                log.error("Save article failed: " + e.getMessage());
                e.printStackTrace();
                String errorStr = "保存失败";
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }


        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }

    }
    /**
     * 添加专题
     * @param  body String
     * @return String
     */
    @RequestMapping("/deleteSpecial")
    public JsonResult deleteSpecial(Model model,
                                    @RequestBody String body) {

        org.json.JSONObject jo = new org.json.JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        White white = isWhiteEnable(whiteId, openId);
        if(white != null){
            Long postId = jo.getLong("postId");

            try {
                Optional<Post> _post = postService.findByPostId(postId);
                if(_post!=null){
                    Post post = _post.get();
                    if(post.getWhite().getWhiteId().equals(whiteId)){

                        List<Attachment> attachments = attachmentService.findByWhiteIdAndPostId(whiteId, postId);
                        //删除照片
                        for (Attachment attachment : attachments) {
                            postService.removeAttachment(attachment);
                        }
                        postService.remove(postId);

                        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), "删除成功");

                    }else {
                        String errorStr = "不是本人不能操作";
                        return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                    }

                }else{
                    String errorStr = "专题不存在";
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);

                }


            } catch (Exception e) {
                log.error("Save article failed: " + e.getMessage());
                e.printStackTrace();
                String errorStr = "保存失败";
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }


        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }

    }
    private String getPostUrl() {
        return UUID.randomUUID().toString();
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




    //======================我的专题======================
    /**
     * 我的专题列表 分页
     * @param page    页码
     * @param  body String
     * @return String
     */
    @RequestMapping("/mySpecial/pages/{page}")
    public JsonResult addSpecial(Model model,
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
            Page<Post> posts = postService.findPostByWhite(white, pageable);
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(),posts);

        }else{
            String errorStr = "账号不可用";
            //System.out.println(errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }
    }












    /**
     * 上传附件
     *
     * @param file    file
     * @param request request
     * @return Map
     */
    @PostMapping(value = "/upload", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResult upload(@RequestParam("file") MultipartFile file,
                                      HttpServletRequest request) {

        if (!file.isEmpty()) {
            try {
                Long whiteId= 0l;
                try {
                    String _whiteId= request.getParameter("whiteId");
                    whiteId = Long.parseLong(_whiteId);
                    System.out.println("whiteId="+whiteId);
                    if(whiteId <= 0){
                        String errorStr = "whiteId不正确";
                        log.error("File upload failed：" + errorStr);
                        return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    String errorStr = "whiteId不能为空";
                    log.error("File upload failed：" + errorStr);
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                }

                Map<String, String> resultMap = attachmentService.upload(file, request);
                if (resultMap == null || resultMap.isEmpty()) {
                    String errorStr = "上传失败1";
                    log.error("File upload failed：" + errorStr);
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                }
                //保存在数据库
                Attachment attachment = new Attachment();
                attachment.setAttachName(resultMap.get("fileName"));
                attachment.setAttachPath(resultMap.get("filePath"));
                attachment.setAttachSmallPath(resultMap.get("smallPath"));
                attachment.setAttachType(file.getContentType());
                attachment.setAttachSuffix(resultMap.get("suffix"));
                attachment.setAttachCreated(DateUtil.date());
                attachment.setAttachSize(resultMap.get("size"));
                attachment.setAttachWh(resultMap.get("wh"));
                attachment.setAttachLocation(resultMap.get("location"));
                attachment.setWhiteId(whiteId);
                attachmentService.save(attachment);
                log.info("Upload file "+resultMap.get("fileName")+" to "+resultMap.get("filePath")+" successfully");
//                result.put("success", ResultCodeEnum.SUCCESS.getCode());
//                result.put("message", localeMessageUtil.getMessage("code.admin.attachment.upload-success"));
//                result.put("url", attachment.getAttachPath());
//                result.put("filename", resultMap.get("filePath"));
//                logsService.save(LogsRecord.UPLOAD_FILE, resultMap.get("fileName"), request);

                return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), attachment.getAttachPath());

            } catch (Exception e) {
                String errorStr = "上传失败2";
                log.error("Upload file failed：" + errorStr +", msg:"+ e.getMessage());
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }
        } else {

            String errorStr = "上传文件不能为空";
            log.error("File upload failed：" + errorStr);
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
        }

    }










}
