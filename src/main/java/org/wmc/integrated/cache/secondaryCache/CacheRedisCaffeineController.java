package org.wmc.integrated.cache.secondaryCache;

import javax.annotation.Resource;

import org.wmc.integrated.mvc.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class CacheRedisCaffeineController {

    @Resource
    private CacheRedisCaffeineService cacheRedisCaffeineService;

    @GetMapping("id/{id}")
    public Result get(@PathVariable long id) {
        UserVO user = cacheRedisCaffeineService.get(id);
        return Result.success(user);
    }

    @GetMapping("name/{name}")
    public Result get(@PathVariable String name) {
        UserVO user = cacheRedisCaffeineService.get(name);
        return Result.success(user);
    }

    @GetMapping("update/{id}")
    public Result update(@PathVariable long id) {
        UserVO user = cacheRedisCaffeineService.get(id);
        cacheRedisCaffeineService.update(user);
        return Result.success(user);
    }

    @GetMapping("delete/{id}")
    public Result delete(@PathVariable long id) {
        cacheRedisCaffeineService.delete(id);
        return Result.success();
    }
}
