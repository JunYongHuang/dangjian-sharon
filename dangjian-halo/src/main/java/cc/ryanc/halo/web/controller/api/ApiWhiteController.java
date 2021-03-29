package cc.ryanc.halo.web.controller.api;

import cc.ryanc.halo.model.domain.*;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.ResponseStatusEnum;
import cc.ryanc.halo.model.enums.ResultCodeEnum;
import cc.ryanc.halo.service.BKCodeService;
import cc.ryanc.halo.service.WhiteService;
import cc.ryanc.halo.service.WxUserService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.dyw.model.WechatInfo;
import com.dyw.utils.SendSms;
import com.dyw.utils.StringUtil;
import com.dyw.utils.WXBizDataCrypt;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping(value = "/api/whites")
public class ApiWhiteController {

    @Autowired
    private WhiteService whiteService;

    @Autowired
    private BKCodeService bkCodeService;

    @Autowired
    private WxUserService wxUserService;


//    /**
//     * 获取所有分类
//     *
//     * <p>
//     *     result json:
//     *     <pre>
//     * {
//     *     "code": 200,
//     *     "msg": "OK",
//     *     "result": [
//     *         {
//     *             "whiteId": "",
//     *             "whiteName": "",
//     *             "whiteUrl": "",
//     *             "whiteDesc": ""
//     *             "count":"文章总数"
//     *         }
//     *     ]
//     * }
//     *     </pre>
//     * </p>
//     *
//     * @return JsonResult
//     */
//    @GetMapping
//    public JsonResult whites() {
//        List<JSONObject> list=new ArrayList<>();
//        List<White> whites = whiteService.findAll();
//        for(White i:whites){
//            JSONObject info=new JSONObject();
//            info.put("whiteId",i.getWhiteId());
//            info.put("count",i.getPosts().size());
//            info.put("whiteName",i.getWhiteName());
//            info.put("whiteUrl",i.getWhiteUrl());
//            info.put("whites",i.getWhiteDesc());
//            info.put("postThumbnail",i.getPostThumbnail());
//            list.add(info);
//
//        }
//        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), list);
//
//
//
//    }

    /**
     * 获取单个分类的信息
     *
     * @return JsonResult
     */
    @RequestMapping(value = "/getOpenidInfo")
    public JsonResult whites(@RequestBody String body) {
        JSONObject jo = new JSONObject(body);

        String code = jo.getString("code");
        if (!StringUtils.isEmpty(code)) {

            Map<String, Object> ret = WxConfigUtil.getOpenIdByCode(code);
            if (ret == null) {
                System.out.println("获取微信消息失败");
                return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
            }else{
                String sessionKey = StringUtil.sNull(ret.get("session_key"));
                String openId = StringUtil.sNull(ret.get("openid"));


                WxUser wxUser = wxUserService.findByOpenId(openId);
                if(wxUser != null){
                    White white =  wxUser.getWhite();
                    if(white != null){
                        if(white.getLoginEnable().equals("true")){
                            white.setOpenId(openId);
                            white.setSessionKey(sessionKey);
                            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), white);
                        }else{
                            String errorStr = "白名单已禁用";
                            //System.out.println(errorStr);
                            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                        }
                    }
                }
                White _white = new White();
                _white.setOpenId(openId);
                _white.setSessionKey(sessionKey);
                return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), _white);

            }

        }
        return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
    }


    /**
     *
     *
     * @return JsonResult
     */
    @RequestMapping(value = "/apply_code")
    public JsonResult sendSms(@RequestBody String body) {
        JSONObject jo = new JSONObject(body);

        String phoneNo = jo.getString("account");
        if (!StringUtils.isEmpty(phoneNo)) {


            White white = whiteService.findByPhoneNo(phoneNo);

            if(white == null){

                String errorStr = "白名单不存在";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }else if(white.getLoginEnable().equals("false")){

                String errorStr = "白名单未启用";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }else if(white.getWxUser() != null){

                String errorStr = "白名单已被绑定";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }




            long phonenum = Long.parseLong(phoneNo);
            BKCode bKCode = bkCodeService.findByPhonenum(phonenum);
            String code;
            Date now = new Date();
            if(bKCode==null) {
                bKCode = new BKCode();
                bKCode.setPhonenum(phonenum);
                code = Math.round((Math.random()+1) * 1000)+"";

            }else {

                long nowTime = now.getTime();

                code = bKCode.getCode();
                Date lastDate = bKCode.getCreateDate();
                long lastTime = lastDate.getTime();

                long reduceTime = nowTime - lastTime;//当前时间-上次的时间
                if(reduceTime < 50000) {//小于50秒 就发送太频繁
                    System.out.println("发送太频繁");
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), "发送太频繁");
                }else if(reduceTime > 600000) {//大于10分钟，则重新生成验证码
                    code = Math.round((Math.random()+1) * 1000)+"";
                }
            }
            bKCode.setCode(code);
            bKCode.setCreateDate(now);
            bkCodeService.save(bKCode);
            SendSms.sendSms(phoneNo, code);

            System.out.println("发送成功 code:" + code);

            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), "发送成功");

        }
        return new JsonResult(ResponseStatusEnum.ERROR.getCode(), "手机号不能为空");
    }

    /**
     *
     *
     * @return JsonResult
     */
    @RequestMapping(value = "/wx_regist")
    @ResponseBody
    public JsonResult wxRegist(@RequestBody String body,
                               HttpServletRequest request) {
        JSONObject jo = new JSONObject(body);

        String phoneNo = jo.getString("account");
        String code = jo.getString("verify_code");
        if (!StringUtils.isEmpty(phoneNo)) {
            long phonenum = Long.parseLong(phoneNo);



            String errorStr = "";

            BKCode bKCode = bkCodeService.findByPhonenumAndCode(phonenum, code);



            if(bKCode==null) {
                errorStr = "验证码不正确";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);

            }else {
                Date now = new Date();
                long nowTime = now.getTime();

                Date lastDate = bKCode.getCreateDate();
                long lastTime = lastDate.getTime();
                if((nowTime - lastTime) > 600000) {
                    errorStr = "验证码已过期";
                    //System.out.println(errorStr);
                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                }
            }


            White white = whiteService.findByPhoneNo(phoneNo);



            if(white == null){

                 errorStr = "白名单不存在";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }else if(white.getLoginEnable().equals("false")){

                 errorStr = "白名单未启用";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }else if(white.getWxUser() != null){

                 errorStr = "白名单已被绑定";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }



            String openid = jo.getString("openid");

            String nickName = jo.getString("nickName");
            String sex = jo.getInt("gender")+"";
            String headUrl = jo.getString("avatarUrl");
            String province = jo.getString("province");
            String city = jo.getString("city");

//            avatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKKf5D5bibBVlTMO6khXsO4FHwjzlLMfBD3OQ0ibFy6athsibUGReibeicrBcU3U5AiaIP84dveVksxyXXw/132"
//            city: ""
//            country: "Reunion"
//            gender: 1
//            language: "zh_CN"
//            nickName: "Hyman😀"
//            province: ""

            WxUser wxUser;
            String ip = ServletUtil.getClientIP(request);
            Date now = new Date();
            wxUser = wxUserService.findByOpenId(openid);
            if(wxUser==null){
                wxUser = new WxUser();
                wxUser.setCreateDate(now);
                wxUser.setOpenId(openid);
            }

            try {
                wxUser.setNickName(nickName);
                wxUser.setSex(sex);
                wxUser.setHeadUrl(headUrl);
                wxUser.setProvince(province);
                wxUser.setCity(city);
                wxUser.setLastLoginDate(now);
                wxUser.setIp(ip);
                wxUser.setWhite(white);
                wxUser = wxUserService.save(wxUser);
                white.setOpenId(openid);

                return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), white);
            } catch (final Exception e) {
                e.printStackTrace();

                log.warn(e.getMessage());
//                throw new Exception("User '" + wxUser.getName() + "' save error!");
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), "保存失败");
            }



        }
        return new JsonResult(ResponseStatusEnum.ERROR.getCode(), "手机号不能为空");
    }

    /**
     *
     *
     * @return JsonResult
     */
    @RequestMapping(value = "/wx_phonenum")
    @ResponseBody
    public JsonResult wxPhonenum(@RequestBody String body,
                               HttpServletRequest request) {
        JSONObject jo = new JSONObject(body);

        String iv = jo.getString("iv");
        String encryptedData = jo.getString("encryptedData");




        String appId = "wxa16fa46d75fa6577";
        String sessionKey = jo.getString("sessionKey");


        WXBizDataCrypt biz = new WXBizDataCrypt(appId, sessionKey);

        String userInfo = biz.decryptData(encryptedData, iv);
        String phoneNo = null;
        try {
            com.alibaba.fastjson.JSONObject jsons = JSON.parseObject(userInfo);
            phoneNo = jsons.getString("phoneNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!StringUtils.isEmpty(phoneNo)) {
            long phonenum = Long.parseLong(phoneNo);



            String errorStr = "";

//            BKCode bKCode = bkCodeService.findByPhonenumAndCode(phonenum, code);
//
//
//
//            if(bKCode==null) {
//                errorStr = "验证码不正确";
//                //System.out.println(errorStr);
//                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
//
//            }else {
//                Date now = new Date();
//                long nowTime = now.getTime();
//
//                Date lastDate = bKCode.getCreateDate();
//                long lastTime = lastDate.getTime();
//                if((nowTime - lastTime) > 600000) {
//                    errorStr = "验证码已过期";
//                    //System.out.println(errorStr);
//                    return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
//                }
//            }


            White white = whiteService.findByPhoneNo(phoneNo);



            if(white == null){

                errorStr = "白名单不存在";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }else if(white.getLoginEnable().equals("false")){

                errorStr = "白名单未启用";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }else if(white.getWxUser() != null){

                errorStr = "白名单已被绑定";
                //System.out.println(errorStr);
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
            }



            String openid = jo.getString("openid");

            String nickName = jo.getString("nickName");
            String sex = jo.getInt("gender")+"";
            String headUrl = jo.getString("avatarUrl");
            String province = jo.getString("province");
            String city = jo.getString("city");

//            avatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKKf5D5bibBVlTMO6khXsO4FHwjzlLMfBD3OQ0ibFy6athsibUGReibeicrBcU3U5AiaIP84dveVksxyXXw/132"
//            city: ""
//            country: "Reunion"
//            gender: 1
//            language: "zh_CN"
//            nickName: "Hyman😀"
//            province: ""

            WxUser wxUser;
            String ip = ServletUtil.getClientIP(request);
            Date now = new Date();
            wxUser = wxUserService.findByOpenId(openid);
            if(wxUser==null){
                wxUser = new WxUser();
                wxUser.setCreateDate(now);
                wxUser.setOpenId(openid);
            }

            try {
                wxUser.setNickName(nickName);
                wxUser.setSex(sex);
                wxUser.setHeadUrl(headUrl);
                wxUser.setProvince(province);
                wxUser.setCity(city);
                wxUser.setLastLoginDate(now);
                wxUser.setIp(ip);
                wxUser.setWhite(white);
                wxUser = wxUserService.save(wxUser);
                white.setOpenId(openid);

                return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), white);
            } catch (final Exception e) {
                e.printStackTrace();

                log.warn(e.getMessage());
//                throw new Exception("User '" + wxUser.getName() + "' save error!");
                return new JsonResult(ResponseStatusEnum.ERROR.getCode(), "保存失败");
            }



        }
        return new JsonResult(ResponseStatusEnum.ERROR.getCode(), "手机号不能为空");
    }
    /**
     * 解绑
     *
     * @return JsonResult
     */
    @RequestMapping(value = "/unBindPhone")
    public JsonResult unBindPhone(@RequestBody String body) {
        JSONObject jo = new JSONObject(body);

        Long whiteId = jo.getLong("whiteId");
        String openId = jo.getString("openId");
        if (whiteId>0 && !StringUtils.isEmpty(openId)) {

                WxUser wxUser = wxUserService.findByOpenId(openId);
                if(wxUser != null){
                    White white =  wxUser.getWhite();
                    if(white != null){
                        if(white.getWhiteId().equals(whiteId)){
                            wxUser.setWhite(null);
                            wxUserService.save(wxUser);
                            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), "解绑成功");
                        }else{
                            String errorStr = "解绑不成功";
                            //System.out.println(errorStr);
                            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), errorStr);
                        }
                    }
                }


        }
        return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
    }



    /**
     * 获取白名单登录信息
     * @return JsonResult
     */
    @RequestMapping(value = "/wx_refresh")
    public JsonResult wx_refresh(
                                 @RequestBody String body) {

        try {
            JSONObject jo = new JSONObject(body);


            Long whiteId = jo.getLong("whiteId");
            String openId = jo.getString("openId");
            White white = isWhiteEnable(whiteId, openId);
            if(white!=null)
                white.setOpenId(openId);
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), white);
        } catch (final Exception e) {
            e.printStackTrace();

            log.warn(e.getMessage());
//                throw new Exception("User '" + wxUser.getName() + "' save error!");
            return new JsonResult(ResponseStatusEnum.ERROR.getCode(), "刷新名单失败");
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
