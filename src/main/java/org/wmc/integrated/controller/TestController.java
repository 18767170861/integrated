package org.wmc.integrated.controller;

import org.wmc.integrated.dao.CourseMapper;
import org.wmc.integrated.entity.Course;
import org.wmc.integrated.mvc.ResponseResult;
import org.wmc.integrated.test.TestService;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * test测试类
 */
@RestController
public class TestController {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TestService testService;

    /**
     * test方法
     */
    @GetMapping("test")
    @ShardingTransactionType(value = TransactionType.LOCAL)
    @Transactional
    @ResponseResult
    public String addCourse() {
        AtomicReference<String> s = new AtomicReference<>("");
        IntStream.rangeClosed(0, 10).forEach(i -> {
            if (i == 6) {
                // throw new RuntimeException("error");
            }
            Course course = new Course();
            //cid由我们设置的策略，雪花算法进行生成（至少70年内生成的id不会重复）
            course.setCname("java");
            course.setUserId((long) i);
            course.setCstatus("Normal");
            int insert = courseMapper.insert(course);
            s.updateAndGet(v -> v + insert);
        });
        return s.get();
    }

    @GetMapping("/test/one")
    public String one() {
        System.out.println("testService：" + testService.getClass());
        return testService.test();
    }
}
