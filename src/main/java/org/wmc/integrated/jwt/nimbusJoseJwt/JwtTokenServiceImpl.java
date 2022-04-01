package org.wmc.integrated.jwt.nimbusJoseJwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.stereotype.Service;
import org.wmc.integrated.jackson.JsonUtils;

import java.text.ParseException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * JWT处理的业务类
 */
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Override
    public String generateTokenByHMAC(String payloadStr, String secret) throws JOSEException {
        //创建JWS头，设置签名算法和类型
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).
                type(JOSEObjectType.JWT)
                .build();
        //将负载信息封装到Payload中
        Payload payload = new Payload(payloadStr);
        //创建JWS对象
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        //创建HMAC签名器
        JWSSigner jwsSigner = new MACSigner(secret);
        //签名
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    @Override
    public PayloadDto verifyTokenByHMAC(String token, String secret) throws ParseException, JOSEException {
        //从token中解析JWS对象
        JWSObject jwsObject = JWSObject.parse(token);
        //创建HMAC验证器
        JWSVerifier jwsVerifier = new MACVerifier(secret);
        if (!jwsObject.verify(jwsVerifier)) {
            throw new RuntimeException("token签名不合法！");
        }
        String payload = jwsObject.getPayload().toString();
        PayloadDto payloadDto = JsonUtils.parse(payload, PayloadDto.class);
        if (payloadDto.getExp() < Instant.now().getEpochSecond()) {
            throw new RuntimeException("token已过期！");
        }
        return payloadDto;
    }

    @Override
    public PayloadDto getDefaultPayloadDto() {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(60 * 60);
        return PayloadDto.builder()
                .sub("macro")
                .iat(now.getEpochSecond())
                .exp(exp.getEpochSecond())
                .jti(UUID.randomUUID().toString())
                .username("macro")
                .authorities(Arrays.asList("ADMIN"))
                .build();
    }

}