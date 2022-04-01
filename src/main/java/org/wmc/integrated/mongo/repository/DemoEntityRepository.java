package org.wmc.integrated.mongo.repository;

import org.wmc.integrated.mongo.entity.DemoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DemoEntityRepository extends MongoRepository<DemoEntity, Long> {
}
