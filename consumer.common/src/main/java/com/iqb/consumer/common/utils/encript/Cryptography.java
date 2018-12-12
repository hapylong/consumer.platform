package com.iqb.consumer.common.utils.encript;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * 3DES加密、解密
 * 
 * @author chris
 * 
 */
public class Cryptography {
    // 约定的key值
    public static final String stringKey = "05i6q7b805i6q7b805i6q7b8";

    /**
     * 加密方法
     * 
     * @param stringKey 加密Key
     * @param stringData 要加密的内容
     * @return 加密后的内容
     * @throws Exception
     */
    // public static String encrypt(String stringData)
    // throws Exception {
    // //BASE64Decoder base64Decoder = new BASE64Decoder();
    // //BASE64Encoder base64Encoder = new BASE64Encoder();
    //
    // byte[] key = stringKey.getBytes("UTF-8");
    // byte[] data = stringData.getBytes("UTF-8");
    //
    // //准备密钥及向量
    // DESedeKeySpec keySpec = new DESedeKeySpec(key);
    // SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
    // Key k = keyfactory.generateSecret(keySpec);
    //
    // //加密
    // Cipher cipher = Cipher.getInstance("DESede");
    // cipher.init(Cipher.ENCRYPT_MODE, k);
    // byte[] bOut = cipher.doFinal(data);
    //
    // String encode = binaryHexString(bOut);
    //
    // return encode;
    // }

    /**
     * 解密
     * 
     * @param stringKey 加密Key
     * @param stringData 要解密的内容
     * @return 解密后的内容
     * @throws Exception
     */
    public static String decrypt(String stringData)
            throws Exception {
        byte[] key = stringKey.getBytes("UTF-8");
        byte[] data = HexStringToBinary(stringData);

        // 准备密钥及向量
        DESedeKeySpec keySpec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        Key k = keyfactory.generateSecret(keySpec);

        // 解密
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, k);
        byte[] bOut = cipher.doFinal(data);
        System.out.println("解密时数组=" + Arrays.toString(bOut));
        return new String(bOut, "UTF-8");
    }

    /**
     * 转为16进制
     * 
     * @param b
     * @return
     */
    public static String binaryHexString(byte[] b) {

        String cOut = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            cOut += hex;
        }
        return cOut.toUpperCase();

    }

    /**
     * 
     * @param hexString
     * @return 将十六进制转换为字节数组
     */
    public static byte[] HexStringToBinary(String hexString) {
        // hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;// 字节高四位
        byte low = 0;// 字节低四位

        for (int i = 0; i < len; i++) {
            // 右移四位得到高位
            high = (byte) ((stringKey.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) stringKey.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);// 高地位做或运算
        }
        return bytes;
    }

    /**
     * 根据输入的字符串生成固定的32位MD5码
     * 
     * @param str 输入的字符串
     * @return MD5码
     */
    public final static String encrypt(String str) {
        MessageDigest mdInst = null;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        mdInst.update(str.getBytes());// 使用指定的字节更新摘要
        byte[] md = mdInst.digest();// 获得密文
        return byteArrToHexStr(md);
    }

    /**
     * 将byte数组转换为表示16进制值的字符串，如：byte[]{8,18}转换为：0813， 和public static byte[] hexStrToByteArr(String
     * strIn) 互为可逆的转换过程
     * 
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     */
    private static String byteArrToHexStr(byte[] arrB) {
        int iLen = arrB.length;
        // 每个byte(8位)用两个(16进制)字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {// 把负数转换为正数
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {// 小于0F的数需要在前面补0
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

}
