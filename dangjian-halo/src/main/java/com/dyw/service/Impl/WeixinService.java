package com.dyw.service.Impl;


import com.dyw.model.WechatInfo;

import com.dyw.utils.HttpUtil;
import com.dyw.utils.StringUtil;
import com.dyw.utils.WxConfigUtil;

import org.nutz.http.Http;
import org.nutz.json.Json;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyw.model.ContentValues;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ：enilu
 * @date ：Created in 1/19/2020 4:31 PM
 */
@Service
public class WeixinService {
//    @Autowired
//    private CfgService cfgService;
//    @Autowired
//    private ShopUserService shopUserService;
//    @Autowired
//    private CacheDao cacheDao;
    private Logger logger = LoggerFactory.getLogger(WeixinService.class);




    private String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

//    public Map<String, String> getSign(String url) {
//        Map<String, String> map = getSign(cfgService.getCfgValue(CfgKey.WX_JS_API_TICKET), url);
//        map.put("appId", ContentValues.APPID);
//        return map;
//    }
//
//    public Map<String, String> getSign(String jsapi_ticket, String url) {
//        Map<String, String> ret = new HashMap<String, String>();
//        String nonce_str = createNonceStr();
//        String timestamp = createTimestamp();
//        String string1;
//        String signature = "";
//
//        // 注意这里参数名必须全部小写，且必须有序
//        string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
//        logger.info(string1);
//        try {
//            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//            crypt.reset();
//            crypt.update(string1.getBytes("UTF-8"));
//            signature = byteToHex(crypt.digest());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        ret.put("url", url);
//        ret.put("jsapi_ticket", jsapi_ticket);
//        ret.put("nonceStr", nonce_str);
//        ret.put("timestamp", timestamp);
//        ret.put("signature", signature);
//
//        return ret;
//    }
}
