package org.wmc.integrated.cors;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // 使用javax.servlet.ServletContainerInitializer方式注册Filter
    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        super.registerDispatcherServlet(servletContext);

        // 注册Jar包内 内置的Filter等等
        UrlBasedCorsConfigurationSource confiurationSource = new UrlBasedCorsConfigurationSource();
        // 根据URL配置其对应的CORS配置 key支持的是AntPathMatcher.match()
        // 说明：这里请使用LinkedHashMap，确保URL的匹配顺序（/**请放在最后一个）
        Map<String, CorsConfiguration> corsConfigs = new LinkedHashMap<>();
        //corsConfigs.put("*", new CorsConfiguration().applyPermitDefaultValues());
        // '/**'表示匹配所有深度的路径
        corsConfigs.put("/**", new CorsConfiguration().applyPermitDefaultValues());
        confiurationSource.setCorsConfigurations(corsConfigs);

        // /*表示所有请求都用此filter处理一下
        servletContext.addFilter("corsFilter", new CorsFilter(confiurationSource))
                .addMappingForUrlPatterns((EnumSet.of(DispatcherType.REQUEST)), false, "/*");
    }

    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }
}