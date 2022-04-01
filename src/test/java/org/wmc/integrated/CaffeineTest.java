package org.wmc.integrated;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.wmc.integrated.bizlog.bean.Order;

public class CaffeineTest extends BaseTest {

    @Qualifier("caffeineCache")
    @Autowired
    Cache<String, Object> cache;


    @Qualifier("caffeineCache2")
    @Autowired
    Cache<String, Object> cache2;

    @Qualifier("FIRST_CACHE")
    @Autowired
    org.springframework.cache.Cache cache3;

    @Qualifier("SECOND_CACHE")
    @Autowired
    org.springframework.cache.Cache cache4;

    @Test
    public void test() {
        Order order = new Order();
        order.setOrderId(2L);
        order.setOrderNo("MT0000011");
        order.setProductName("超值优惠红烧肉套餐");
        order.setPurchaseName("张三");
        cache.put("order", order);
        Object order1 = cache.getIfPresent("order");
        System.out.println("order1:" + order1);
        System.out.println("order2:" + cache2.getIfPresent("order"));

        cache3.put("order3", order1);
        System.out.println("cache3:" + cache3.get("order3", Order.class));
        System.out.println("cache3:" + cache4.get("order3", Order.class));
        cache4.put("order4", order1);
        System.out.println("cache4:" + cache3.get("order4", Order.class));
        System.out.println("cache4:" + cache4.get("order4", Order.class));

    }


}
