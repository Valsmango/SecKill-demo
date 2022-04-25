package com.ymt.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * MD5工具类
 * @author ymt
 * @since 1.0.0
 */
// 有弹幕说工具类不应该加Component注解
@Component
public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    // 第一次加密：这里的两次加密规则需要和login.html中的加密规则一致，但是……为什么？
    public static String inputPassToFromPass(String inputPass) {
        // JDK自动将String转变为StringBuffer
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    // 第二次加密：
    public static String fromPassToDBPass(String fromPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        // d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(inputPassToFromPass("123456"));
        // 数据库中数据应当为：b7797cce01b4b131b433b6acf4add449
        // 这里的第一次salt应该和login.html中的salt一致
        System.out.println(fromPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9", "1a2b3c4d"));
        // c8803b1180685be4af90c3ef18eb3713
        System.out.println(fromPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9", "2b3c4d1a"));
        // c8803b1180685be4af90c3ef18eb3713
        System.out.println(inputPassToDBPass("123456","2b3c4d1a"));
    }

}
