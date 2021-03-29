package com.dyw.service.Impl;

//import com.dyw.dao.LackDao;
//import com.dyw.dao.MovieDao;
//import com.dyw.model.*;
//import com.dyw.service.TBKService;
import com.dyw.service.WechatService;
import com.dyw.utils.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号接口
 * Created by dyw on 2017/10/16.
 */
@Service
public class WechatServiceImpl implements WechatService {

    private static final Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

//    @Autowired
//    private MovieDao movieDao;
//
//    @Autowired
//    private LackDao lackDao;
//
//    @Autowired
//    private TBKService tbkService;

    @Override
    public String weixinPost(HttpServletRequest request) {
        String respMessage = null;
//        try {
//            // xml请求解析
//            Map<String, String> requestMap = MessageUtil.xmlToMap(request);
//            // 发送方帐号（open_id）
//            String fromUserName = requestMap.get("FromUserName");
//            // 公众帐号
//            String toUserName = requestMap.get("ToUserName");
//            // 消息类型
//            String msgType = requestMap.get("MsgType");
//            // 消息内容
//            String content = requestMap.get("Content");
//            logger.info("FromUserName is:" + fromUserName + ", ToUserName is:" + toUserName + ", MsgType is:" + msgType);
//
//            logger.error("查询内容：" + content + "==========================================================");
//            // 文本消息
//            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT) && !"【收到不支持的消息类型，暂无法显示】".equals(content)) {
//                //去掉空格中英文逗号这些容易误写的。
//                String key = content.replace(" ", "").replace(",", "").replace("，", "");
//                //这里根据关键字执行相应的逻辑，只有你想不到的，没有做不到的
//                List<Movie> movieByName = movieDao.findMovieByName(key);
//                String st = "";
//                if (movieByName.size() == 0) {
//                    Lack lack = new Lack();
//                    lack.setCreateTime(new Date());
//                    lack.setLackName(content);
//                    lackDao.insertLack(lack);
//                    st = "sorry~~~ " +
//                            "阿扑没找到您想要的资源（＞﹏＜）请检查输入名称是否正确。如若问题依旧，不妨精简片名重新尝试，例如“行尸走肉第七季”改为“行尸走肉”或“行尸”";
//               } else if (movieByName.size() > 0) {
//                    List<Item> list = new ArrayList<>();
//                    int count = movieByName.size();
//                    if (count > 8) {
//                        count = 8;
//                    }
//                    for (int i = 0; i < count; i++) {
//                        Movie m = movieByName.get(i);
//                        Item item = new Item();
//                        item.setDescription("   ");
//                        item.setPicUrl(null);
//                        String replace = m.getMovieName().replace("\n", "");
//                        String replace1 = replace.replace("\r", "");
//                        item.setTitle(replace1);
//                        String replace2 = m.getAddress().replace("\n", "");
//                        String replace3 = replace2.replace("\r", "");
//                        item.setUrl(replace3);
//                        list.add(item);
//                    }
//                    PTMessage ptMessage = new PTMessage();
//                    ptMessage.setToUserName(fromUserName);
//                    ptMessage.setFromUserName(toUserName);
//                    ptMessage.setCreateTime(new Date().getTime() + "");
//                    ptMessage.setMsgType("news");
//                    ptMessage.setArticleCount(count);
//                    Articles articles = new Articles();
//                    articles.setItem(list);
//                    ptMessage.setArticles(articles);
//                    respMessage = MessageUtil.textMessageToXml(ptMessage);
//                    String replace = respMessage.replace("<item>", "");
//                    String replace1 = replace.replace("</item>", "");
//                    String replace2 = replace1.replace("com.dyw.model.I", "i");
//                    String replace3 = replace2.replace("com.dyw.model.I", "");
//                    return replace3;
//                }
//                //自动回复
//                TextMessage text = new TextMessage();
//                text.setContent(st);
//                text.setToUserName(fromUserName);
//                text.setFromUserName(toUserName);
//                text.setCreateTime(new Date().getTime() + "");
//                text.setMsgType(msgType);
//                respMessage = MessageUtil.textMessageToXml(text);
//            } /*else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {// 事件推送
//                String eventType = requestMap.get("Event");// 事件类型
//
//                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {// 订阅
//                    respContent = "欢迎关注xxx公众号！";
//                    return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
//                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {// 自定义菜单点击事件
//                    String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
//                    logger.info("eventKey is:" +eventKey);
//                    return xxx;
//                }
//            }
//            //开启微信声音识别测试 2015-3-30
//            else if(msgType.equals("voice"))
//            {
//                String recvMessage = requestMap.get("Recognition");
//                //respContent = "收到的语音解析结果："+recvMessage;
//                if(recvMessage!=null){
//                    respContent = TulingApiProcess.getTulingResult(recvMessage);
//                }else{
//                    respContent = "您说的太模糊了，能不能重新说下呢？";
//                }
//                return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
//            }
//            //拍照功能
//            else if(msgType.equals("pic_sysphoto"))
//            {
//
//            }
//            else
//            {
//                return MessageResponse.getTextMessage(fromUserName , toUserName , "返回为空");
//            }*/
//            // 事件推送
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
//                String eventType = requestMap.get("Event");// 事件类型
//                // 订阅
//                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//                    TextMessage text = new TextMessage();
//                    text.setContent("亲，你终于来了\n" +
//                            "\n" +
//                            "回复电影或者电视剧名称即可观看\n" +
//                            "\n" +
//                            "热门资源请尽快保存，如果资源失效，请不要着急，我们马上就会补上\n" +
//                            "\n" +
//                            "谢谢关注~么么哒\n" +
//                            "\n" +
//                            "如有什么疑问，也可直接回复留言，我们会尽快给您答复~");
//                    text.setToUserName(fromUserName);
//                    text.setFromUserName(toUserName);
//                    text.setCreateTime(new Date().getTime() + "");
//                    text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//                    respMessage = MessageUtil.textMessageToXml(text);
//                } else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅
//
//                }
//                // 自定义菜单点击事件
//                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
//                    String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
//                    if ("customer_telephone".equals(eventKey)) {
//                        TextMessage text = new TextMessage();
//                        text.setContent("0755-86671980");
//                        text.setToUserName(fromUserName);
//                        text.setFromUserName(toUserName);
//                        text.setCreateTime(new Date().getTime() + "");
//                        text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//
//                        respMessage = MessageUtil.textMessageToXml(text);
//                    }
//                }
//            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
//                TextMessage text = new TextMessage();
//                text.setContent("sorry，阿扑看不懂[委屈]");
//                text.setToUserName(fromUserName);
//                text.setFromUserName(toUserName);
//                text.setCreateTime(new Date().getTime() + "");
//                text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//                respMessage = MessageUtil.textMessageToXml(text);
//            } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT) && "【收到不支持的消息类型，暂无法显示】".equals(content)) {
//                TextMessage text = new TextMessage();
//                text.setContent("sorry，阿扑看不懂[委屈]");
//                text.setToUserName(fromUserName);
//                text.setFromUserName(toUserName);
//                text.setCreateTime(new Date().getTime() + "");
//                text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//                respMessage = MessageUtil.textMessageToXml(text);
//            }
//        } catch (Exception e) {
//            logger.error("error......");
//            throw new RuntimeException(e);
//        }
        return respMessage;
    }
}
