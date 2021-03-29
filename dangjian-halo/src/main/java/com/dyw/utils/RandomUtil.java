package com.dyw.utils;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
 
/**
 * 随机数、随机字符串生成工具
 */
public class RandomUtil  extends Random {
    /**
     * 所有大小写字母和数字
     */
    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 所有大小写字母
     */
    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 所有数字
     */
    public static final String numberChar = "0123456789";
     
    /**
     * 返回一个定长的随机纯数字字符串(只包含数字)
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateDigitalString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return sb.toString();
    }
 
    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }
 
    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateMixString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }
 
    /**
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }
 
    /**
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }
 
    /**
     * 生成一个定长的纯0字符串
     * @param length
     *            字符串长度
     * @return 纯0字符串
     */
    public static String generateZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }
 
    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     * @param num
     *            数字
     * @param fixdlenth
     *            字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(long num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
                    + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }
 
    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     * @param num
     *            数字
     * @param fixdlenth
     *            字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(int num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
                    + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }
     
     
    /**  
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）  
     *  
     * @since 1.0.0  
     */  
     
    private static final long serialVersionUID = 1L;
 
    public static RandomUtil getInstance() {
        return _instance;
    }
 
    public RandomUtil() {
        super();
    }
 
    public RandomUtil(long seed) {
        super(seed);
    }
 
    public int[] nextInt(int n, int size) {
        if (size > n) {
            size = n;
        }
 
        Set set = new LinkedHashSet();
 
        for (int i = 0; i < size; i++) {
            while (true) {
                Integer value = new Integer(nextInt(n));
 
                if (!set.contains(value)) {
                    set.add(value);
 
                    break;
                }
            }
        }
 
        int[] array = new int[set.size()];
 
        Iterator itr = set.iterator();
 
        for (int i = 0; i < array.length; i++) {
            array[i] = ((Integer)itr.next()).intValue();
        }
 
        return array;
    }
 
    public void randomize(char array[]) {
        int length = array.length;
 
        for(int i = 0; i < length - 1; i++) {
            int x = nextInt(length);
            char y = array[i];
 
            array[i] = array[i + x];
            array[i + x] = y;
 
            length--;
        }
    }
 
    public void randomize(int array[]) {
        int length = array.length;
 
        for(int i = 0; i < length - 1; i++) {
            int x = nextInt(length);
            int y = array[i];
 
            array[i] = array[i + x];
            array[i + x] = y;
 
            length--;
        }
    }
 
    public void randomize(List list) {
        int size = list.size();
 
        for(int i = 0; i <= size; i++) {
            int j = nextInt(size);
            Object obj = list.get(i);
 
            list.set(i, list.get(i + j));
            list.set(i + j, obj);
 
            size--;
        }
    }
 
    public void randomize(Object array[]) {
        int length = array.length;
 
        for(int i = 0; i < length - 1; i++) {
            int x = nextInt(length);
            Object y = array[i];
 
            array[i] = array[i + x];
            array[i + x] = y;
 
            length--;
        }
    }
 
    public String randomize(String s) {
        if (s == null) {
            return null;
        }
 
        char[] array = s.toCharArray();
 
        randomize(array);
 
        return new String(array);
    }
 
    private static RandomUtil _instance = new RandomUtil();
}