package com.milisong.wechat.miniapp.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomUtils {

    private static String[] chars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static int bound = 36;                                                                          // 区间
    private static int length = 10;                                                                          // 获取字符串长度，默认10

    /**
     * 获取随机字符串
     *
     * @return code 随机字符串
     */
    public static String generateCode() {
        String code = "";
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int m = random.nextInt(bound);
            code += chars[m];
        }
        return code;
    }

    /**
     * 获取随机字符串
     *
     * @param len 字符串长度
     * @return code 随机字符串
     */
    public static String generateCode(int len) {
        String code = "";
        for (int i = 0; i < len; i++) {
            Random random = new Random();
            int m = random.nextInt(bound);
            code += chars[m];
        }
        return code;
    }

    /**
     * 获取随机兑奖码
     *
     * @param num 个数
     * @return codes 兑奖码
     */
    public static Set<String> getCodes(int num) {
        Set<String> codes = new HashSet<String>();
        for (int i = 0; i < num; i++) {
            String code = generateCode();
            codes.add(code);
        }
        return codes;
    }

    /**
     * 获取随机兑奖码
     *
     * @param num 个数
     * @param len 每个长度
     * @return codes 兑奖码
     */
    public static Set<String> getCodes(int num, int len) {
        Set<String> codes = new HashSet<String>();
        for (int i = 0; i < num; i++) {
            String code = generateCode(len);
            codes.add(code);
        }
        return codes;
    }

    /**
     * 生成自定义长度随机字符串
     *
     * @param len  字符串长度
     * @param type 字符串类型 0 ：纯数字、1： 纯大写字母、2：纯小写字母、3：数字加大写字母、4：数字加小写字母、其他：数字加大小写字母
     * @return String 随机字符串
     */
    public static String getRandomString(int len, int type) {
        String rand = "";
        String[] chars = null;
        if (type == 0) {
            chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        } else if (type == 1) {
            chars = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                    "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        } else if (type == 2) {
            chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
                    "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        } else if (type == 3) {
            chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
                    "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        } else if (type == 4) {
            chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g",
                    "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        } else if (type == 5) {
            chars = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        } else {
            chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
                    "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                    "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
                    "v", "w", "x", "y", "z"};
        }

        Random random = new Random();
        for (int i = 0; i < len; i++) {
            rand = rand + chars[random.nextInt(chars.length)];
        }
        return rand;
    }

}
