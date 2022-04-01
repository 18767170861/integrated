package org.wmc.integrated;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.wmc.integrated.mybatisplus.User;
import org.wmc.integrated.mybatisplus.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

class MybatisPlusApplicationTests extends BaseTest {
    // 继承了BaseMapper，所有的方法都来自己父类
    // 我们也可以编写自己的扩展方法！
    @Autowired
    @Qualifier("userMapperPlus")
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        // 参数是一个 Wrapper ，条件构造器，这里我们先设置条件为空，查询所有。
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    // 测试插入
    @Test
    public void testInsert() {
        User user = new User();
        user.setName("kwhua_mybatis-plus_insertTest");
        user.setAge(15);
        user.setEmail("310697723@qq.com");

        int result = userMapper.insert(user); // 帮我们自动生成id
        System.out.println(result); // 受影响的行数
        System.out.println(user); // 看到id会自动填充。
    }

    // 更新操作
    @Test
    public void testUpdate() {
        User user = new User();
        // 通过条件自动拼接动态sql
        user.setId(1374532102517624835L);
        user.setName("kwhua_mybatis-plus_updateTest");
        user.setAge(25);
        // 注意：updateById 但是参数是一个对象！
        int i = userMapper.updateById(user);
        System.out.println(i);
    }

    // 测试乐观锁成功！
    @Test
    public void testOptimisticLocker() {
        // 1、查询用户信息
        User user = userMapper.selectById(1L);
        System.out.println("user:" + user);
        // 2、修改用户信息
        user.setName("kwhua");
        user.setEmail("123456@qq.com");
        user.setAge(88);
        // 3、执行更新操作
        userMapper.updateById(user);
    }

    // 测试乐观锁失败！多线程下
    @Test
    public void testOptimisticLocker2() {

        // 线程 1
        User user = userMapper.selectById(1L);
        user.setName("kwhua111");
        user.setEmail("123456@qq.com");

        // 模拟另外一个线程执行了插队操作
        User user2 = userMapper.selectById(1L);
        user2.setName("kwhua222");
        user2.setEmail("123456@qq.com");
        userMapper.updateById(user2);

        // 自旋锁来多次尝试提交！
        userMapper.updateById(user); // 如果没有乐观锁就会覆盖插队线程的值！
    }

    // 测试分页查询
    @Test
    public void testPage() {
        // 参数一：当前页
        // 参数二：页面大小
        Page<User> page = new Page<>(2, 10);
        userMapper.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
    }

    // 测试删除
    @Test
    public void testDeleteById() {
        userMapper.deleteById(4L);
    }

}
