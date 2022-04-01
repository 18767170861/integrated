package org.wmc.integrated.mongo;

import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MongoTemplateHelper {

    @Getter
    private MongoTemplate mongoTemplate;

    public MongoTemplateHelper(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 保存记录
     *
     * @param params
     * @param collectionName
     */
    public void saveRecord(Map<String, Object> params, String collectionName) {
        mongoTemplate.save(params, collectionName);
    }

    /**
     * 精确查询方式
     *
     * @param query
     * @param collectionName
     */
    public void queryRecord(Map<String, Object> query, String collectionName) {
        Criteria criteria = null;
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            if (criteria == null) {
                criteria = Criteria.where(entry.getKey()).is(entry.getValue());
            } else {
                criteria.and(entry.getKey()).is(entry.getValue());
            }
        }

        Query q = new Query(criteria);
        Map result = mongoTemplate.findOne(q, Map.class, collectionName);
        log.info("{}", result);
    }
}
