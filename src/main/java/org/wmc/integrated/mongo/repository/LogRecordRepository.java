package org.wmc.integrated.mongo.repository;

import org.wmc.integrated.bizlog.bean.LogRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRecordRepository extends MongoRepository<LogRecord, Integer> {
}
