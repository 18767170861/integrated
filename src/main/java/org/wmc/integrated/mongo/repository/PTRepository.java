package org.wmc.integrated.mongo.repository;

import org.wmc.integrated.mongo.entity.PT;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PTRepository extends MongoRepository<PT, String> {
}
