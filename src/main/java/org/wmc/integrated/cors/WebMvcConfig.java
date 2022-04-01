package org.wmc.integrated.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 对应于xml
     * <mvc:cors>
     * 	<mvc:mapping path="/test/cors" ... />
     *     <mvc:mapping path="/**" ... />
     * </mvc:cors>
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/test/cors")
                // -------addMapping后还可以继续配置-------
                .allowedOrigins("http://localhost:63342")
                .maxAge(300L);
        registry.addMapping("/**").allowedOrigins("*");
    }
}
