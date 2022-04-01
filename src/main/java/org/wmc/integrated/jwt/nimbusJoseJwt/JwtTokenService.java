package org.wmc.integrated.jwt.nimbusJoseJwt;

import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface JwtTokenService {

    String generateTokenByHMAC(String payloadStr, String secret) throws JOSEException;

    PayloadDto verifyTokenByHMAC(String token, String secret) throws ParseException, JOSEException;

    PayloadDto getDefaultPayloadDto();
}