package com.dyw.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.nutz.http.Http;
import org.nutz.json.Json;
import org.nutz.log.Logs;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.dyw.model.ContentValues;
import com.dyw.model.Token;
import com.dyw.model.WechatInfo;


/**
 * 公众平台通用接口工具类
 *
 * @author james
 * @date 2015-02-27
 */
public class WxConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger(WxConfigUtil.class);

    // 获取access_token的接口地址（GET） 限2000（次/天）
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    // 获取jsapi_ticket的接口地址（GET） 限2000（次/天）
    public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    // 缓存添加的时间
    public static String cacheAddTime = null;
    // token,ticket缓存
    public static Map<String, Token> TOKEN_TICKET_CACHE = new HashMap<String, Token>();
    // token对应的key
    private static final String TOKEN = "token";
    // ticket对应的key
    private static final String TICKET = "ticket";

    /**
     * 外部获取签名入口类
     *
     * @param appUrl 应用的url
     * @return
     */
    public static Map<String, Object> getSignature(String appUrl, String appId, String secret, String url, String urlpath) {
        // 生成签名的随机串
        String noncestr = RandomUtil.generateString(4);
        logger.warn("-=-=-=-=-=-=-=-=appUrl:" + appUrl + ", logger.isInfoEnabled():" + logger.isInfoEnabled());
        if (appUrl == null || "".equals(appUrl)) {
            return null;
        }
        
        String signature = null;
        Token accessTocken = getToken(appId, secret, System.currentTimeMillis() / 1000);
        String token = accessTocken.getToken();
        logger.info("-=-=-=-=-=-=-=-=token:" + token);
        if(token == null) {        	
        	logger.error("token 获取失败， 详情看上面的 errcode。");
        }
        Token accessTicket = getTicket(token, System.currentTimeMillis() / 1000);
        logger.info("-=-=-=-=-=-=-=-=ticket:" + accessTicket.getTicket());
        signature = signature(accessTicket.getTicket(), cacheAddTime, noncestr, appUrl);
        logger.info("-=-=-=-=-=-=-=-=signature:" + signature);
        logger.info("-=-=-=-=-=-=-=-=timestamp:" + cacheAddTime);
        Map<String, Object> map = new HashMap<>();
        map.put("appId", appId);
        map.put("timestamp", cacheAddTime);
        map.put("nonceStr", noncestr);
        map.put("appUrl", appUrl);
        map.put("signature", signature);
        map.put("url", url);
        map.put("urlpath", urlpath);
        logger.info("返回参数获取成功:" + map.toString() + map);
        return map;
    }

    /**
     * 获得Token
     *
     * @return
     */
    public static String getToken(String appId, String secret) {
        Token accessTocken = getToken(appId, secret, System.currentTimeMillis() / 1000);
        return accessTocken.getToken();
    }

    /**
     * 签名
     *
     * @param timestamp
     * @return
     */
    private static String signature(String jsapi_ticket, String timestamp, String noncestr, String url) {
        jsapi_ticket = "jsapi_ticket=" + jsapi_ticket;
        timestamp = "timestamp=" + timestamp;
        noncestr = "noncestr=" + noncestr;
        url = "url=" + url;
        String[] arr = new String[]{jsapi_ticket, noncestr, timestamp, url};
        // 将token、timestamp、nonce,url参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
            if (i != arr.length - 1) {
                content.append("&");
            }
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        content = null;
        return tmpStr;
    }

    /**
     * 获取access_token
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return
     */
    public static Token getToken(String appid, String appsecret, long currentTime) {
        Token tockenTicketCache = getTokenTicket(TOKEN);
        Token Token = null;

        if (tockenTicketCache != null && (currentTime - tockenTicketCache.getAddTime() <= tockenTicketCache.getExpiresIn())) {// 缓存存在并且没过期
            logger.info("==========缓存中token已获取时长为：" + (currentTime - tockenTicketCache.getAddTime()) + "毫秒，可以重新使用");
            return tockenTicketCache;
        }
        logger.warn("==========缓存中token不存在或已过期===============");
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        logger.warn("==========getToken方法===============：" + jsonObject);
        // 如果请求成功
        if (null != jsonObject) {
            Token = new Token();
            Token.setToken(jsonObject.getString("access_token"));
            Token.setExpiresIn(jsonObject.getIntValue("expires_in") / 2);// 正常过期时间是7200秒，此处设置3600秒读取一次
            logger.info("==========tocket缓存过期时间为:" + Token.getExpiresIn() + "毫秒");
            Token.setAddTime(currentTime);
            updateToken(TOKEN, Token);
        }
        return Token;
    }

    /**
     * 获取ticket
     *
     * @param token
     * @return
     */
    private static Token getTicket(String token, long currentTime) {
        Token tockenTicketCache = getTokenTicket(TICKET);
        Token Token = null;
        if (tockenTicketCache != null && (currentTime - tockenTicketCache.getAddTime() <= tockenTicketCache.getExpiresIn())) {// 缓存中有ticket
            logger.info("==========缓存中ticket已获取时长为：" + (currentTime - tockenTicketCache.getAddTime()) + "毫秒，可以重新使用");
            return tockenTicketCache;
        }
        logger.info("==========缓存中ticket不存在或已过期===============");
        String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        logger.info("==========getTicket方法===============：" + jsonObject);
        if (null != jsonObject) {
            Token = new Token();
            Token.setTicket(jsonObject.getString("ticket"));
            Token.setExpiresIn(jsonObject.getIntValue("expires_in") / 2);// 正常过期时间是7200秒，此处设置3600秒读取一次
            logger.info("==========ticket缓存过期时间为:" + Token.getExpiresIn() + "毫秒");
            Token.setAddTime(currentTime);
            updateToken(TICKET, Token);
        }
        return Token;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    private static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
            // jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            logger.info("Weixin server connection timed out.");
        } catch (Exception e) {
            logger.info("https request error:{}" + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {

        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    /**
     * 从缓存中读取token或者ticket
     *
     * @return Token
     */
    private static Token getTokenTicket(String key) {
        if (TOKEN_TICKET_CACHE != null && TOKEN_TICKET_CACHE.get(key) != null) {
            Token token = TOKEN_TICKET_CACHE.get(key);
            logger.info("[getTokenTicket 获取token]" + token + "成功===============");
            return token;
        }
        return null;
    }

    /**
     * 更新缓存中token或者ticket
     *
     * @return
     */
    private static void updateToken(String key, Token accessTocken) {
        if (TOKEN_TICKET_CACHE != null && TOKEN_TICKET_CACHE.get(key) != null) {
            TOKEN_TICKET_CACHE.remove(key);
            logger.info("==========从缓存中删除" + key + "成功===============");
        }
        TOKEN_TICKET_CACHE.put(key, accessTocken);
        cacheAddTime = String.valueOf(accessTocken.getAddTime());// 更新缓存修改的时间
        logger.info("==========更新缓存中" + key + "成功===============");
    }

	
	
	

    public static Map<String, Object> getPrivateAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
		url += "?appid=" + ContentValues.APPID;
        url += "&secret=" + ContentValues.SECRET;
        url += "&js_code=" + code;
        url += "&grant_type=authorization_code";
        try {
            String result = Http.get(url).getContent();
            Object object = Json.fromJson(StringUtil.sNull(result));

            logger.info("url:" + url + ";  response:" + Json.toJson(object));
            String session_key = (String) Mapl.cell(object, "session_key");
            if (StringUtil.isNotEmpty(session_key)) {
                return (Map) object;
            }
        } catch (Exception e) {
            logger.error("获取token失败", e);
        }
        return null;
    }


    private  static  boolean isInit;
    public static Map<String, Object> getOpenIdByCode(String code) {
        if(!isInit){
            Logs.init();
            isInit = true;
        }


        Map<String, Object> ret = getPrivateAccessToken(code);
        logger.info("获取token:{}", Json.toJson(ret));
        if (ret != null && ret.get("errcode") == null) {
//            String session_key = StringUtil.sNull(ret.get("session_key"));
//            String openId = StringUtil.sNull(ret.get("openid"));
            return ret;
        }
        return null;
    }

    public static WechatInfo getWechatInfo(String access_token, String openId) {
        //String accessToken = WxConfigUtil.getToken(ContentValues.APPID, ContentValues.SECRET);
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openId +"&lang=zh_CN";
        String result = Http.get(url).getContent("UTF-8");

        Object object = Json.fromJson(StringUtil.sNull(result));

        logger.info("getWecchatInfo====url:" + url + ";  response:" + Json.toJson(object));
        if (Mapl.cell(object, "errcode") != null) {
            logger.info("获取微信用户基本信息失败", Mapl.cell(object, "errmsg"));
        } else {
            if (StringUtil.equals(StringUtil.sNull(Mapl.cell(object, "errcode")), "0")) {
                logger.error("用户:{}没有关注该公众号", openId);
            } else {
                WechatInfo wechatInfo = new WechatInfo();
                wechatInfo.setOpenId(openId);
                wechatInfo.setNickName(StringUtil.sNull(Mapl.cell(object, "nickname")));
                wechatInfo.setHeadUrl(StringUtil.sNull(Mapl.cell(object, "headimgurl")));
                wechatInfo.setSex(StringUtil.sNull(Mapl.cell(object, "sex")));
                wechatInfo.setProvince(StringUtil.sNull(Mapl.cell(object, "province")));
                wechatInfo.setCity(StringUtil.sNull(Mapl.cell(object, "city")));
                wechatInfo.setCountry(StringUtil.sNull(Mapl.cell(object, "country")));
                return wechatInfo;
            }
        }
        return null;
    }
    
    
    
    public static void main(String[] args) {
		String s = "556,-54,637,-80,863,-204,910,-237,934,-288,931,-347,894,-393,803,-446,890,-476,979,-413,1000,-384,1013,-340,1005,-304,978,-264,925,-226,886,-195,865,-155,861,-103,879,-42,916,-4,991,22,1094,49,1117,74,983,51,890,46,801,68,-47,339,-68,322,-75,295,-72,269,-63,253,-52,244,-45,266,-26,292,0,306,61,301,249,213,325,145,350,84,350,20,327,-34,281,-84,238,-106,195,-104,161,-85,145,-61,138,-30,141,5,144,30,8,-92,43,-124,65,-153,99,-174,137,-184,186,-169,249,-145,308,-115,368,-96,407,-94,425,-65,448,-48,481,-43,549,-53";
		//String s = "702,-69,804,-102,1088,-258,1148,-299,1178,-363,1174,-438,1128,-496,1013,-563,1123,-601,1235,-521,1261,-484,1278,-429,1268,-384,1234,-333,1167,-286,1118,-247,1091,-196,1086,-131,1108,-54,1155,-6,1250,28,1379,63,1409,94,1240,65,1123,59,1010,86,-60,428,-86,406,-95,373,-92,340,-80,320,-66,308,-57,336,-34,369,1,386,77,380,315,269,410,184,442,107,442,26,413,-44,355,-107,301,-134,246,-132,204,-108,183,-77,175,-38,178,7,182,39,11,-117,55,-157,83,-194,126,-220,173,-233,235,-214,315,-184,389,-145,465,-122,514,-119,536,-83,565,-61,607,-55,693,-68";
    	//String s = "815,521";
    	String[] strs = s.split(",");
		String cc = "";
		for(int i = 0; i<strs.length; i++) {
			int x = 0;
			int str = Integer.parseInt(strs[i]) ;
			if(i%2 == 0) {
				x = (int) (str*2);
			}else {
				x = (int) (str*2);
				
			}
			cc += (x+",");
		}
		System.out.println(cc);
    }

}
