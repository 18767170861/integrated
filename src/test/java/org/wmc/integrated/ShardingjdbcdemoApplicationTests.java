package org.wmc.integrated;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.wmc.integrated.dao.CourseMapper;
import org.wmc.integrated.dao.UdictMapper;
import org.wmc.integrated.dao.UserMapper;
import org.wmc.integrated.entity.Course;
import org.wmc.integrated.entity.Udict;
import org.wmc.integrated.entity.User;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

class ShardingjdbcdemoApplicationTests extends org.wmc.integrated.BaseTest {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UdictMapper udictMapper;

    //添加课程
    @ShardingTransactionType(value = TransactionType.XA)
    @Transactional(rollbackFor = Exception.class)
    @Rollback(false)
    @Test
    public void addCourse() {
        IntStream.rangeClosed(0, 10).forEach(i -> {
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiii:" + i);
            if (i == 6) {
                throw new RuntimeException("error");
            }
            Course course = new Course();
            //cid由我们设置的策略，雪花算法进行生成（至少70年内生成的id不会重复）
            course.setCname("java");
            course.setUserId((long) i);
            course.setCstatus("Normal");
            courseMapper.insert(course);
        });
    }

    //查询课程
    @Test
    public void findCourse() {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
////        wrapper.eq("cid", 509755853058867201L);
////        courseMapper.selectOne(wrapper);
//        wrapper.in("cid", 2, 5);
//        //wrapper.in("user_id", Arrays.asList(2, 5, 8)).orderByDesc("user_id").orderByAsc("cid");
//        //wrapper.eq("userId", 8);
//        List<Course> courses = courseMapper.selectList(wrapper);
        wrapper.eq("cid", 1L);
        List<Course> course = courseMapper.selectByJoin(2L);
        System.out.println("course:" + course);
    }

    //添加用户
    @Test
    public void addUserDb() {
        User user = new User();
        user.setUsername("张三");
        user.setUstatus("a");
        userMapper.insert(user);
    }

    @Test
    //查询用户
    public void findUserDb() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", 1L);
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    //添加
    @Test
    public void addDict() {
        Udict udict = new Udict();
        udict.setUstatus("a");
        udict.setUvalue("已启用");
        udictMapper.insert(udict);
    }

    @Test
    //查询用户
    public void findDict() {
        QueryWrapper<Udict> wrapper = new QueryWrapper<>();
        List<Udict> udicts = udictMapper.selectList(wrapper);
        System.out.println(udicts);
    }

    //删除
    @Test
    public void deleteDict() {
        QueryWrapper<Udict> wrapper = new QueryWrapper<>();
        //wrapper.eq("dictid", 509811689974136833L);
        udictMapper.delete(wrapper);
    }
}
