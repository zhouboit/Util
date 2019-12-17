package com.jonbore.Util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author bo.zhou
 * @date 2019/12/10
 */
public class Random {

    public static void main(String[] args) {
        //验证重复率
        repeatTest(2, 60001);
    }

    private static void repeatTest(int byteNum, int boundary) {
        Map<String, String> integerMap = new HashMap<>(boundary);
        for (int i = 0; i < boundary; i++) {
            String random = nextHex(byteNum);
            System.out.println(random);
            integerMap.put(random, random);
        }
        System.out.println("生成次数： " + boundary);
        System.out.println("生成结果： " + integerMap.size());
        System.out.println("重复率： " + (1.0 - (double) integerMap.size() / boundary));
    }

    /**
     * 需要生成的随机数字节数
     * PS：经过验证4字节（4294967295）的随机数已经不能通过原生工具类生成，所以考虑按字节生成随机数，无位数限制
     * 返回值已经排除了全0和全F的结果
     *
     * @param byteNum
     * @return
     */
    private static String nextHex(int byteNum) {
        String random;
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            StringBuilder stringBuilder = new StringBuilder();
            for (int num = byteNum; num > 0; num--) {
                //生成一字节十六进制随机数 0～254
                Integer randomNum;
                if (byteNum==1) {
                    //生成一字节十六进制随机数 0～254
                     randomNum = secureRandom.nextInt(255);
                } else {
                    //生成一字节十六进制随机数 0～254
                     randomNum = secureRandom.nextInt(256);
                }
                //十进制转16进制字符串（字母转大写）
                String hexStr = Integer.toHexString(randomNum).toUpperCase();
                //保证生成结果包含高低位，位数不足高位补0
                String fillBit = ("00" + hexStr).substring(("00" + hexStr).length() - 2);
                stringBuilder.append(fillBit);
            }
            random = stringBuilder.toString();
            if (validate(random)) {
                return random;
            }
            return nextHex(byteNum);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 校验输入字符串是否为全0和是否为全F
     *
     * @param content
     * @return
     */
    private static boolean validate(String content) {
        //正则匹配全0
        String fullZero = "^[0]+$";
        //正则匹配全F
        String fullF = "^[F]+$";
        //全0校验
        Pattern pattern = Pattern.compile(fullZero);
        if (!pattern.matcher(content).matches()) {
            //全F校验 理论上SecureRandom生成的结果不会为全F
            pattern = Pattern.compile(fullF);
            //只有content不为全0和content不为全F的结果才会返回true，
            //其他情况都返回false
            return !pattern.matcher(content).matches();
        } else {
            return false;
        }
    }
}
