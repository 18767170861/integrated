package org.wmc.integrated.jwt.nimbusJoseJwt;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName: PayloadDto
 * @Description:
 **/
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class PayloadDto {
    // 主题
    private String sub;
    // 签发时间
    private Long iat;
    // 过期时间
    private Long exp;
    // JWT的ID
    private String jti;
    // 用户名称
    private String username;
    // 用户拥有的权限
    private List<String> authorities;
}