package org.wmc.integrated.httpclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "httpclient.custom")
public class HttpClientProperties {
    private Integer maxTotal;//最大连接数
    private Integer defaultMaxPerRoute;//支持并发数
    private Integer connectTimeout;//连接的超时时间
    private Integer connectionRequestTimeout;//从连接池获取连接的超时时间
    private Integer socketTimeout;//数据传输超时时间
}
