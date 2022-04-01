package org.wmc.integrated;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.retry.annotation.EnableRetry;
import org.wmc.integrated.mongo.MongoTemplateHelper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@MapperScan({"org.wmc.integrated.dao"})
@EnableLogRecord(tenant = "org.wmc.integrated")
@EnableRetry
@SpringBootApplication
public class IntegratedApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(IntegratedApplication.class, args);
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        Arrays.asList(beanDefinitionNames).forEach(System.out::println);
    }

    public IntegratedApplication(MongoTemplateHelper mongoTemplateHelper) {
        System.out.println("ShardingjdbcdemoApplication init");
        Map<String, Object> records = new HashMap<>(4);
        records.put("_id", "60614ad140bfa72e506f81872");
        records.put("name", "小灰灰Blog-------------------");
        records.put("github", "https://github.com/liuyueyi");
        records.put("time", LocalDateTime.now());

        mongoTemplateHelper.saveRecord(records, COLLECTION_NAME);

        Map<String, Object> query = new HashMap<>(4);
        query.put("name", "小灰灰Blog");
        mongoTemplateHelper.queryRecord(query, COLLECTION_NAME);

    }

    private static final String COLLECTION_NAME = "personal_info";
}
