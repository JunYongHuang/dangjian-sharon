package com.dyw.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {

//    public static String basePath = "https://moohoy.goho.co";
    /**
     * 得到网页中图片的地址
     * @return
     */
    public static Map getImgStr(String htmlStr, Boolean isFull){
        Map pics = new HashMap();
        String img="";
        Pattern p_image;
        Matcher m_image;
//        List<String> pics = new ArrayList<String>();
        //去除域名
        String pattern = "^((http://)|(https://))?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}(/)";

        String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        p_image = Pattern.compile
                (regEx_img,Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        String path;
        while(m_image.find()){
            img = img + "," + m_image.group();
            Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
            while(m.find()){
                path = m.group(1);
                if(!isFull){//不要完整的，则去除https://xxx:9090
                    int index = path.indexOf("/upload/");
                    path = path.substring(index);
                }

                pics.put(path, true);
//                pics.add(m.group(1));
            }
        }
        return pics;
    }
    //重点在于正则表达式 <img.*src=(.*?)[^>]*?>
    //               src=\"?(.*?)(\"|>|\\s+)


    public static void main(String[] args) {
        String pattern = "^((http://)|(https://))?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}(/)";
        Pattern p = Pattern.compile(pattern);
        String line = "https://xxx.yyy.cn:9090/upload/2020/bb.jpg";

        int index = line.indexOf("/upload/");
        String _line = line.substring(index);
        System.out.println("_line:" + _line);
        Matcher m = p.matcher(line);

        if(m.find()){
            //匹配结果
            System.out.println("=" + m.group());
        }
        //替换
        System.out.println(line.replaceAll(pattern, ""));
    }

}
