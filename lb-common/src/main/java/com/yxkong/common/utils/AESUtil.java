package com.yxkong.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），是一种区块加密标准。这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用。
 * <p>
 * 那么为什么原来的DES会被取代呢，，原因就在于其使用56位密钥，比较容易被破解。而AES可以使用128、192、和256位密钥，并且用128位分组加密和解密数据，相对来说安全很多。
 * 完善的加密算法在理论上是无法破解的，除非使用穷尽法。使用穷尽法破解密钥长度在128位以上的加密数据是不现实的，仅存在理论上的可能性。
 * 统计显示，即使使用目前世界上运算速度最快的计算机，穷尽128位密钥也要花上几十亿年的时间，更不用说去破解采用256位密钥长度的AES算法了。
 * @Author: yxkong
 * @Date: 2021/6/28 2:23 下午
 * @version: 1.0
 */
@Slf4j
public class AESUtil {

    //不允许改变此SEED
    private static final String SEED = "yxkong_token";

    private static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";

    /**加密解密发生异常*/
    private static final String AES_DES_EXCEPTION = "加密解密发生异常";


    private AESUtil(){}

    public static String encrypt(String content){
        return encrypt(content,SEED);
    }

    /**
     * AES加密，得到十六进制结果
     *
     * @param content
     * @return
     */
    public static String encrypt(String content,String seed) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            byte[] encrypt = encryptToByte(content,SEED);
            return parseByte2HexStr(encrypt);
        } catch (Exception e) {
            log.error(AES_DES_EXCEPTION, e);
            return null;
        }
    }
    public static String decrypt(String content) {
        return decrypt(content,SEED);
    }
    /**
     * AES解密
     *
     * @param content
     * @return
     */
    public static String decrypt(String content,String seed) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            byte[] decrypt = parseHexStr2Byte(content);
            return decryptToByte(decrypt,SEED);
        } catch (Exception e) {
            log.error(AES_DES_EXCEPTION, e);
            return null;
        }
    }
    /**
     * AES加密字符串.此处得到的是二进制
     *
     * @param content 需要被加密的字符串
     * @return 密文
     */
    private static byte[] encryptToByte(String content,String seed) {
        try {
            // 创建AES的Key生产者
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //解决linux下面每次生成加密不一样的问题
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed.getBytes());
            // 利用KEY作为随机数初始化出
            kgen.init(128, secureRandom);
            // 根据用户密码，生成一个密钥
            SecretKey secretKey = kgen.generateKey();
            // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            byte[] enCodeFormat = secretKey.getEncoded();
            // 转换为AES专用密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");

            byte[] byteContent = content.getBytes("utf-8");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            log.error(AES_DES_EXCEPTION, e);
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content AES加密过过的内容
     * @return 明文
     */
    private static String decryptToByte(byte[] content,String seed) {
        try {
            // 创建AES的Key生产者
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed.getBytes());
            // 利用KEY作为随机数初始化出
            kgen.init(128, secureRandom);
            // 根据用户密码，生成一个密钥
            SecretKey secretKey = kgen.generateKey();
            // 返回基本编码格式的密钥
            byte[] enCodeFormat = secretKey.getEncoded();
            // 转换为AES专用密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            // 明文
            return new String(result);

        } catch (Exception e) {
            log.error(AES_DES_EXCEPTION, e);
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}