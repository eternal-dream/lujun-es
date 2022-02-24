package com.cqvip.innocence.common.util.encryption;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

/**
 * @ClassName SM2Util
 * @Description 国密2加密算法基于hutool工具包的通用工具
 * 使用时，执行一次getKeyPair(),将获得的公私钥保存，后续加解密使用保存的公私钥
 * @Author Innocence
 * @Date 2021/8/25 18:12
 * @Version 1.0
 */
public class SM2Util {

    public static final String PRIAVTEKEY = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQg+gbKKLtuW8D6JzFrjEd0IPZ3eIzw" +
            "N3rRVuIzNGPYpKigCgYIKoEcz1UBgi2hRANCAAQCiaM2DlrO/J1xWOBsNilAlaQKfNvhcXyMRC7ynsl6WUA6nDJK4S9qHJtdPLRXgBIhy/p" +
            "Tw+L1IAeWJv2TCiRS";

    public static final String PUBLICKEY = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEAomjNg5azvydcVjgbDYpQJWkCnzb4XF8jEQu8p7J" +
            "ellAOpwySuEvahybXTy0V4ASIcv6U8Pi9SAHlib9kwokUg==";

    public static final String PUBLICQKEY = "040289a3360e5acefc9d7158e06c36294095a40a7cdbe1717c8c442ef29ec97a59403a9c324" +
            "ae12f6a1c9b5d3cb457801221cbfa53c3e2f520079626fd930a2452";

    @Data
    @AllArgsConstructor
    public static class SM2KeyPair{
        /**
         * 公钥
         */
        private String publicKey;

        /*vue js端 公钥*/
        private String publicQKey;

        /**
         * 私钥
         */
        private String privateKey;
    }

    /**
     * 生成密钥对
     * @author Innocence
     * @date 2021/8/26
     * @return com.cqvip.innocence.common.util.encryption.SM2UtilWtihHutool.SM2KeyPair
     */
    public SM2KeyPair getKeyPair() {
        SM2 sm2= SmUtil.sm2();
        /*公钥*/
        String publicKey=sm2.getPublicKeyBase64();
        /* js端公钥 JS代码实现 SM2的方案，都是直接使用的私钥的d值和公钥的q值直接进行的加解密所以后端口返回的最好是从公钥里面提取的q值，以q值做为js端的加密公钥*/
        String publicQKey = HexUtil.encodeHexStr(((BCECPublicKey)sm2.getPublicKey()).getQ().getEncoded(false));
        /*私钥*/
        String privateKey=sm2.getPrivateKeyBase64();
        return new SM2KeyPair(publicKey,publicQKey,privateKey);
    }

    /**
     * 公钥加密
     * @author Innocence
     * @date 2021/8/26
     * @param data 需要加密的数据
     * @param publicKey  公钥
     * @return java.lang.String
     */
    public String encrypt(String data, String publicKey) {
        SM2 sm2=SmUtil.sm2(null,publicKey);
        return sm2.encryptBcd(data, KeyType.PublicKey);
    }

    /**
     * 私钥解密 公钥加密密文
     * @author Innocence
     * @date 2021/8/26
     * @param encryptStr 密文
     * @param privateKey 私钥
     * @return java.lang.String
     */
    public String decrypt(String encryptStr, String privateKey){
        if(!encryptStr.startsWith("04")){
            encryptStr="04".concat(encryptStr);
        }
        SM2 sm2=SmUtil.sm2(privateKey,null);
        return StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
    }

    /**
     * 私钥签名
     * @author Innocence
     * @date 2021/8/26
     * @param data 签名字符串
     * @param privateKey 私钥
     * @return java.lang.String
     */
    public String signByPrivateKey(String data, String privateKey){
        SM2 sm2= SmUtil.sm2(privateKey,null);
        String sign = sm2.signHex(HexUtil.encodeHexStr(data));
        return sign;
    }

    /**
     * 公钥验签
     * @author Innocence
     * @date 2021/8/26
     * @param data 签名原文
     * @param publicKey 公钥
     * @param signature 签名
     * @return boolean
     */
    public boolean verifyByPublicKey(String data, String publicKey, String signature){
        SM2 sm2= SmUtil.sm2(null,publicKey);
        boolean verify = sm2.verifyHex(HexUtil.encodeHexStr(data), signature);
        return verify;
    }

}
