package org.wmc.integrated.mongo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.wmc.integrated.mongo.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

    Page<User> findByUserNameLike(String userName, Pageable pageable);
}
