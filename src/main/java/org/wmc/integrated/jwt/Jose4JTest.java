package org.wmc.integrated.jwt;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import java.security.Key;
import java.util.UUID;

public class Jose4JTest {

    /**
     * keyId,公钥,私钥 都是用 createKeyPair 方法生成
     */
    private static String keyId = "fa677d525c0e4ee485a61543937794af";
    private static String privateKeyStr = "{\"kty\":\"RSA\",\"kid\":\"fa677d525c0e4ee485a61543937794af\",\"alg\":\"RS256\",\"n\":\"ki6NKW2ow53FBjWf21xNGF0v-Fzv9R4-vu5tz7LHz4vZ7TE2Lp7Xx0N2vFIH-HLZPLfAYW35W5iV29sW_MkbhVlh6f0q4AeCIeYrVjBGbcYTK5g-Sb8i9sO78DkGivryKTU4tUnOjqar2bfobwXScFhAgc4-BjIcvZ9V8LEzcAW76he500-sqekXqvYv7LbxIMlbadGEwbBqjscE83hiYjk1KSFrEeNWKP6E0X_cHVGEEGys8IKlBcfwOOCgaJ0sCFxvN3M54V33jSUknFzHAi1qJRsOI87-Fk1oYS-aniQOTfm5y5x1syTIgWEX9JvXCQgTxjp2kMItuoL2G2faoQ\",\"e\":\"AQAB\",\"d\":\"OmSUCN-AEZv9LwzOrW6CcWAQIHLne4-4WsadYOE2hcaEqAYHcboL0dI2JOXTv0AJXQK9u22VtSwPeMJcvV-MOclJnpF9xf3Z0rbByuz_xSvhToHDJ-xNCCuJ8FynK28wuptC6s_vzfXwIckf9PFrbWsjYXbEOe9cobZ7Ould9boHkEq-x0eKkDFfcQbevuQeM54FCk5FGl_D4wpbuqudZiT8FYCJPO7m-MTnND6xzdEVuApuQUPCjAin34ygp9QPk2VfQaM1z-ZHchTaXtsv5gyLwu3bqA91vugmrtGuSBG8pFaN6mYL_ivt1Q_7QDLqXZ5_WBhkoXO5DZ4-6ApLvQ\",\"p\":\"58fTzOMjU1zJ42ZdUkJjSpRlZXdlWx3ILStrvVw7kCHH1iBZ4Lckd7SdqJ9DUfdIBFTNPaAKfDqpoz7fWOUZyyUpy7_8qaDagFi53pAjuCGUuKnOV5hbSPBIOvGi9iTHwLNz6Om5eRV6lBXoTRJgT7g8u88HeSyl29RmYt-N7rs\",\"q\":\"oXTxPC0BFDxpFjk2Jpuj6hBkioD3Kiv7HEJjxRoOL3TEAAc3811f7V7bO5FWK2L_2g8I5YiZhsyh0SyxjPBfuIzcbAfGSTAsFqKiIOGZ7fqiFTOV319FJIkcMOrT2dbvGxNeGgTJpWB1vcX6C8i3ytzPFto1H9Bl3xAPVFflHFM\",\"dp\":\"RU-eaLCryav_u37K_WRY6N6Di9oudxbq24cWiuPf8_QGHGREPEzIHPvoAZrOuN4nrRPm5DzNpeStAeI1TBIGqpcMbp-U4Oz3KlZeDs4vwEpafPZafBtVgPRJxUapIs5Q5bFEQixSiIEBzPLYKuQJ5Q0FLGx2oafWWWykyYBsoy0\",\"dq\":\"SS2_yQ581rcqyi_UI1uXx5b2evBJFowonH5ayhMtKsU5sOmUqnE_8U50_2K4M6IDZMo7tg1byIUnMq-XKdIpEHSH008SyElVMk00PsMCCaL3o7Rl0YBUzmJ2rJVCwBFy_kqg9BoHazV1KDZ7RqwK4Z-DHVB5k5nZEmktCYVtCpE\",\"qi\":\"jDbVP3xGaYx2wgAGJT7KNjdoC4dYbl5ajp9saRCgAih3TYr1CjZSrLRm9L90UukgwajU0pHaffP74epVx0RBhnS5GtZhCoGitpLwYSDkZ9qTMTVnFPHrg6M1OYEEXjZ_UKlnYCrzrDe6tfHcO1UTzMzsuOgHjRAV7IS6ImfzwVw\"}";
    private static String publicKeyStr = "{\"kty\":\"RSA\",\"kid\":\"fa677d525c0e4ee485a61543937794af\",\"alg\":\"RS256\",\"n\":\"ki6NKW2ow53FBjWf21xNGF0v-Fzv9R4-vu5tz7LHz4vZ7TE2Lp7Xx0N2vFIH-HLZPLfAYW35W5iV29sW_MkbhVlh6f0q4AeCIeYrVjBGbcYTK5g-Sb8i9sO78DkGivryKTU4tUnOjqar2bfobwXScFhAgc4-BjIcvZ9V8LEzcAW76he500-sqekXqvYv7LbxIMlbadGEwbBqjscE83hiYjk1KSFrEeNWKP6E0X_cHVGEEGys8IKlBcfwOOCgaJ0sCFxvN3M54V33jSUknFzHAi1qJRsOI87-Fk1oYS-aniQOTfm5y5x1syTIgWEX9JvXCQgTxjp2kMItuoL2G2faoQ\",\"e\":\"AQAB\"}";

    public static long accessTokenExpirationTime = 60 * 60 * 24;

    /**
     * 创建 keyId 公钥 私钥
     */
    public static void createKeyPair(){
        String keyId = UUID.randomUUID().toString().replaceAll("-", "");
        RsaJsonWebKey jwk = null;
        try {
            jwk = RsaJwkGenerator.generateJwk(2048);
        } catch (JoseException e) {
            e.printStackTrace();
        }
        jwk.setKeyId(keyId);
        //采用的签名算法 RS256
        jwk.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
        String publicKey = jwk.toJson(RsaJsonWebKey.OutputControlLevel.PUBLIC_ONLY);
        String privateKey = jwk.toJson(RsaJsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);

        System.out.println("keyId="+keyId);
        System.out.println();
        System.out.println("公钥 publicKeyStr="+publicKey);
        System.out.println();
        System.out.println("私钥 privateKeyStr="+privateKey);
    }

    public static void main(String[] args) throws JoseException {
        testRSA();
    }

    public static void testHMAC() throws JoseException {
        // 注意密钥长短（最少32个字符）
        HmacKey hmacKey = new HmacKey("12345678123456781234567812345678".getBytes());
        JsonWebSignature jsonWebSignature = jsonWebSignature(hmacKey, AlgorithmIdentifiers.HMAC_SHA256);
        String jwt = jsonWebSignature.getCompactSerialization();
        JwtConsumer jwtConsumer = jwtConsumer(hmacKey, AlgorithmIdentifiers.HMAC_SHA256);
        try {
            // 校验 JWT 并将其处理成 JwtClaims 对象
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            System.out.println("JWT validation succeeded! JwtClaims: " + jwtClaims);
        } catch (InvalidJwtException e) {
            handleException(e);
        }
    }

    public static void testRSA() throws JoseException {
        // 生成 RSA 密钥对（打包在 JsonWebKey 中），将用于签名和验签 JWT
        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        // 给 JsonWebKey 赋值一个密钥 ID（可选操作）
        rsaJsonWebKey.setKeyId("key1");
        //采用的签名算法 RS256
        rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
        JsonWebSignature jsonWebSignature = jsonWebSignature(rsaJsonWebKey.getPrivateKey(),
                rsaJsonWebKey.getKeyId(),
                AlgorithmIdentifiers.RSA_USING_SHA256);
        /*
         * 签名 JWS，生成 JWT 字符串
         * 如果想加密此字符串，则将此结果作为 JsonWebEncryption 对象的负载，并将 cty（Content Type）头设置为 jwt
         */
        String jwt = jsonWebSignature.getCompactSerialization();
        /*
         * 使用 JwtConsumerBuilder 构建一个合适的 JwtConsumer 对象，用于校验和处理 JWT。
         * 如果 JWT 已被加密，只需提供一个解密密钥或解密密钥解析器给 JwtConsumerBuilder。
         */
        JwtConsumer jwtConsumer = jwtConsumer(rsaJsonWebKey.getKey(), AlgorithmIdentifiers.RSA_USING_SHA256);
        try {
            // 校验 JWT 并将其处理成 JwtClaims 对象
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            System.out.println("JWT validation succeeded! JwtClaims: " + jwtClaims);
        } catch (InvalidJwtException e) {
            handleException(e);
        }
    }

    private static JsonWebSignature jsonWebSignature(Key key, String algorithm) {
        return jsonWebSignature(key, null, algorithm);
    }

    /**
     * 一个 JWT 是一个携带 JwtClaims 作为负载的 JsonWebSignature 或 JsonWebEncryption 对象
     * 本例中的 JWT 是一个 JsonWebSignature 对象
     */
    private static JsonWebSignature jsonWebSignature(Key key, String kid, String algorithm) {
        JsonWebSignature jsonWebSignature = new JsonWebSignature();
        // 为 JsonWebSignature 对象添加负载：JwtClaims 对象的 Json 内容
        jsonWebSignature.setPayload(jwtClaims().toJson());
        // JWT 使用 RSA 私钥签名
        jsonWebSignature.setKey(key);
        // 可选操作
        if (null != key) {
            jsonWebSignature.setKeyIdHeaderValue(kid);
        }
        // 在 JWT / JWS 上设置签名算法
        jsonWebSignature.setAlgorithmHeaderValue(algorithm);
        return jsonWebSignature;
    }

    /**
     * 创建 Claims，包装了 JWT 的内容
     */
    private static JwtClaims jwtClaims() {
        JwtClaims claims = new JwtClaims();
        // 设置 Token 的签发者
        claims.setIssuer("Issuer");
        // 设置过期时间
        // claims.setExpirationTime();
        // 设置过期时间为 10 分钟后
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setSubject("Subject");
        // 设置 Token 将被发送给哪些对象
        claims.setAudience("Audience X", "Audience Y", "Audience Z");
        // claims.setNotBefore();
        // 设置生效时间为 2 分钟前
        claims.setNotBeforeMinutesInThePast(2);
        // claims.setIssuedAt();
        // 设置 Token 发布/创建 时间为当前时间
        claims.setIssuedAtToNow();
        // claims.setJwtId();
        // 为 JWT 设置一个自动生成的唯一 ID
        claims.setGeneratedJwtId();
        // 额外添加的生命属性
        claims.setClaim("email", "email@example.com");
        return claims;
    }

    /**
     * 使用 JwtConsumerBuilder 构建一个合适的 JwtConsumer 对象，用于校验和处理 JWT。
     * 如果 JWT 已被加密，只需提供一个解密密钥或解密密钥解析器给 JwtConsumerBuilder。
     */
    private static JwtConsumer jwtConsumer(Key key, String algorithm) {
        return new JwtConsumerBuilder()
                // 在验证时间时留出一些余量以解决时钟偏差问题
                .setAllowedClockSkewInSeconds(30)
                // 设置解密密钥
                // .setDecryptionKey()
                // 设置解密密钥解析器
                // .setDecryptionKeyResolver()
                // .setDisableRequireSignature()
                // 必须设置过期时间
                .setRequireExpirationTime()
                // 必须设置 Subject
                .setRequireSubject()
                // 必须设置 Token 签发者
                .setExpectedIssuer("Issuer")
                // 必须设置 Token 签发给谁
                .setExpectedAudience("Audience X")
                // 设置用于验证签名的公钥
                .setVerificationKey(key)
                // 设置允许的预期签名算法
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.WHITELIST, algorithm)
                .build();
    }

    /**
     * 处理校验 JWT 并将其处理成 JwtClaims 对象过程中出现的异常
     */
    private static void handleException(InvalidJwtException e) {
        System.out.println("Invalid JWT:" + e);
        try {
            JwtClaims jwtClaims = e.getJwtContext().getJwtClaims();
            // 异常是否因 JWT 过期触发
            if (e.hasExpired()) {
                System.out.println("Expired at " + jwtClaims.getExpirationTime());
            }
            // 异常是否因 Audience 无效触发
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {
                System.out.println("Invalid audience: " + jwtClaims.getAudience());
            }
            // 异常是否因缺少 Audience 触发
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_MISSING)) {
                System.out.println("Audience missing！");
            }
            // 异常是否因缺少加密触发
            if (e.hasErrorCode(ErrorCodes.ENCRYPTION_MISSING)) {
                System.out.println("Encryption missing！");
            }
            // 异常是否因缺少过期时间触发
            if (e.hasErrorCode(ErrorCodes.EXPIRATION_MISSING)) {
                System.out.println("Expiration missing!");
            }
            // 异常是否因过期时间太长触发
            if (e.hasErrorCode(ErrorCodes.EXPIRATION_TOO_FAR_IN_FUTURE)) {
                System.out.println("Expiration too far in future: " + jwtClaims.getExpirationTime());
            }
            // 异常是否因缺乏完整性触发
            if (e.hasErrorCode(ErrorCodes.INTEGRITY_MISSING)) {
                System.out.println("Integrity missing!");
            }
            // 异常是否因发布时间无效触发
            if (e.hasErrorCode(ErrorCodes.ISSUED_AT_INVALID_FUTURE)) {
                System.out.println("Issued at invalid future: " + jwtClaims.getIssuedAt());
            }
            // 异常是否因发布时间无效触发
            if (e.hasErrorCode(ErrorCodes.ISSUED_AT_INVALID_PAST)) {
                System.out.println("Issued at invalid past: " + jwtClaims.getIssuedAt());
            }
            // 异常是否因缺少发布时间触发
            if (e.hasErrorCode(ErrorCodes.ISSUED_AT_MISSING)) {
                System.out.println("Issued at missing!");
            }
            // 异常是否因签发者无效触发
            if (e.hasErrorCode(ErrorCodes.ISSUER_INVALID)) {
                System.out.println("Issuer invalid: " + jwtClaims.getIssuer());
            }
            // 异常是否因缺少签发者触发
            if (e.hasErrorCode(ErrorCodes.ISSUER_MISSING)) {
                System.out.println("Issuer missing!");
            }
            // 异常是否因 JSON 无效触发
            if (e.hasErrorCode(ErrorCodes.JSON_INVALID)) {
                System.out.println("Json invalid: " + jwtClaims.toString());
            }
            // 异常是否因缺少 JWT ID 触发
            if (e.hasErrorCode(ErrorCodes.JWT_ID_MISSING)) {
                System.out.println("JWT ID missing!");
            }
            // 异常是否因 JwtClaims 格式错误触发
            if (e.hasErrorCode(ErrorCodes.MALFORMED_CLAIM)) {
                System.out.println("Malformed claim!");
            }
            // 异常是否因缺少生效时间触发
            if (e.hasErrorCode(ErrorCodes.NOT_BEFORE_MISSING)) {
                System.out.println("Not before missing!");
            }
            // 异常是否因 Token 尚未生效触发
            if (e.hasErrorCode(ErrorCodes.NOT_YET_VALID)) {
                System.out.println("Not yet valid: " + jwtClaims.getNotBefore());
            }
            // 异常是否因 Token 的 Signature 部分无效触发
            if (e.hasErrorCode(ErrorCodes.SIGNATURE_INVALID)) {
                System.out.println("Signature invalid: " + jwtClaims.toString());
            }
            // 异常是否因 Token 的 Signature 部分缺失触发
            if (e.hasErrorCode(ErrorCodes.SIGNATURE_MISSING)) {
                System.out.println("Signature missing!");
            }
            // 异常是否因 Subject 无效触发
            if (e.hasErrorCode(ErrorCodes.SUBJECT_INVALID)) {
                System.out.println("Subject invalid: " + jwtClaims.getSubject());
            }
            // 异常是否因 Subject 缺失触发
            if (e.hasErrorCode(ErrorCodes.SUBJECT_MISSING)) {
                System.out.println("Subject missing!");
            }
            // 异常是否因 Type 无效触发
            if (e.hasErrorCode(ErrorCodes.TYPE_INVALID)) {
                System.out.println("Type invalid: " + jwtClaims.getRawJson());
            }
            // 异常是否因 Type 缺失触发
            if (e.hasErrorCode(ErrorCodes.TYPE_MISSING)) {
                System.out.println("Type missing!");
            }
        } catch (MalformedClaimException e1) {
            System.out.println("Malformed claim: " + e);
        }
    }
}
