package com.dyw.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
/**
 * 对微信小程序用户加密数据的解密
 * 创建者 柒
 * 创建时间 2018年3月12日
 */ public class WXBizDataCrypt {

    public static String illegalAesKey = "-41001";//非法密钥
    public static String illegalIv = "-41002";//非法初始向量
    public static String illegalBuffer = "-41003";//非法密文
    public static String decodeBase64Error = "-41004"; //解码错误
    public static String noData = "-41005"; //数据不正确
    private String appid;

    private String sessionKey;

    public WXBizDataCrypt(String appid, String sessionKey) {
        this.appid = appid;
        this.sessionKey = sessionKey;
    }

    /**
     * 检验数据的真实性，并且获取解密后的明文.
     * @param encryptedData  string 加密的用户数据
     * @param iv  string 与用户数据一同返回的初始向量
     * @return data string 解密后的原文
     * @return String 返回用户信息
     */ public String decryptData(String encryptedData, String iv) {
        if (StringUtils.length(sessionKey) != 24) {
            return illegalAesKey;
        }
        // 对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
        byte[] aesKey = Base64.decodeBase64(sessionKey);

        if (StringUtils.length(iv) != 24) {
            return illegalIv;
        }
        // 对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
        byte[] aesIV = Base64.decodeBase64(iv);

        // 对称解密的目标密文为 Base64_Decode(encryptedData)
        byte[] aesCipher = Base64.decodeBase64(encryptedData);

        try {
            byte[] resultByte = AESUtil.decrypt(aesCipher, aesKey, aesIV);
            if (null != resultByte && resultByte.length > 0) {
                String userInfo = new String(resultByte, "UTF-8");
                JSONObject jsons = JSON.parseObject(userInfo);
                String id = jsons.getJSONObject("watermark").getString("appid");
                if (!StringUtils.equals(id, appid)) {
                    return illegalBuffer;
                }
                return userInfo;
            } else {
                return noData;
            }
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * encryptedData 和 iv 两个参数通过小程序wx.getUserInfo()方法获取
     * @param args
     * @see
     */ public static void main(String[] args) {
        String appId = "wxa16fa46d75fa6577";
        String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
        String encryptedData = "CtG9J/xZEDkOp4c5Vr+LFwXelBs5Us+7n0y+v7VSyuhMgdqFqwZrsc1EUM+zfaOtjjLzslBGgOV0NulomGzOLgO8p7EXWhf6xT6Ztb2dgq/1JTV52ci7D3xxTg1k4CfrStMa0rHKUq7k0n4FAML5R6cluWlwNELz2Av+Qa13Cr+ojxxgXIbqK1jK0SMQ6xeuDL5ssOwyxl95a9qA6BcSZbDQ==";
        String iv = "fnGyAwdybmMexdEdb+BlBTXa5g==";

        WXBizDataCrypt biz = new WXBizDataCrypt(appId, sessionKey);

        System.out.println(biz.decryptData(encryptedData, iv));

    }
}