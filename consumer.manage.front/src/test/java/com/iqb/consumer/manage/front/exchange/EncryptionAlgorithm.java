package com.iqb.consumer.manage.front.exchange;

import org.apache.commons.lang.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ckq.
 */
public class EncryptionAlgorithm {

    public static void main(String[] args) {
        String random = RandomStringUtils.random(20, true, true);
        System.out.println(random);
        int randomNumber = (int) (Math.random() * 16 + 1);
        Integer randomInt = randomNumber;
        String encrypte = encrypte("192.168.0.1", random, randomInt, new Date());
        System.out.println(encrypte + " " + encrypte.length());
        System.out.println(random + randomInt.toString());
        boolean isOk = decode(encrypte, random + randomInt.toString());
        System.out.println(isOk);
    }

    /**
     * 生成密文的算法
     * 
     * @param ip 咨询范的ip
     * @param randomString 20位的随机字符串
     * @param randomInt 1位1-15的随机数
     * @param nowTime 现在时间
     * @return
     */
    public static String encrypte(String ip, String randomString, Integer randomInt, Date nowTime) {
        StringBuffer ciphertext = new StringBuffer();
        ciphertext.append(ip + "d");
        String substring = randomString.substring(randomInt - 1, randomInt + 4);
        ciphertext.append(substring + "d");
        String s = randomString + randomInt.toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s1 = simpleDateFormat.format(nowTime);
        ciphertext.append(s1);
        byte[] bytes = ciphertext.toString().getBytes();
        StringBuffer stringOut = new StringBuffer();
        for (Byte b : bytes) {
            Integer sbMid = b.intValue() ^ randomInt;
            stringOut.append(sbMid.toString() + "d");
        }
        return stringOut.toString();
    }

    public static boolean decode(String ciphertext, String express) {
        String number = "";
        if (express.length() == 21) {
            number = express.substring(20, 21);
        }
        if (express.length() == 22) {
            number = express.substring(20, 22);
        }
        int randomNumber = Integer.parseInt(number);
        String[] ds = ciphertext.split("d");
        byte[] bytes = new byte[40];
        int count = 0;
        for (String s : ds) {
            int xor = Integer.parseInt(s);
            Integer xorMid = xor ^ randomNumber;
            byte b = xorMid.byteValue();
            bytes[count] = b;
            count++;
        }
        String s1 = new String(bytes);

        String[] ds1 = s1.split("d");
        String ip = ds1[0];
        if ("192.168.0.1".equals(ip))
            return true;
        return false;
    }
}
