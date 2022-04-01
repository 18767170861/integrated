package org.wmc.integrated.cache.secondaryCache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class CacheRedisCaffeineService {


    @Cacheable(key = "'cache_user_id_' + #id", value = "userIdCache", cacheManager = "redisCaffeineCacheManager")
    public UserVO get(long id) {
        log.info("get by id from db");
        UserVO user = new UserVO();
        user.setId(id);
        user.setName("name" + id);
        user.setCreateTime(new Date());
        return user;
    }

    @Cacheable(key = "'cache_user_name_' + #name", value = "userNameCache", cacheManager = "redisCaffeineCacheManager")
    public UserVO get(String name) {
        log.info("get by name from db");
        UserVO user = new UserVO();
        user.setId(new Random().nextLong());
        user.setName(name);
        user.setCreateTime(new Date());
        return user;
    }

    @CachePut(key = "'cache_user_id_' + #userVO.id", value = "userIdCache", cacheManager = "redisCaffeineCacheManager")
    public UserVO update(UserVO userVO) {
        log.info("update to db");
        userVO.setCreateTime(new Date());
        return userVO;
    }

    @CacheEvict(key = "'cache_user_id_' + #id", value = "userIdCache", cacheManager = "redisCaffeineCacheManager")
    public void delete(long id) {
        log.info("delete from db");
    }
}