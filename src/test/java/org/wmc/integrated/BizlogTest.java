package org.wmc.integrated;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wmc.integrated.bizlog.bean.Order;
import org.wmc.integrated.bizlog.service.IOrderService;

public class BizlogTest extends BaseTest {

    @Autowired
    private IOrderService orderService;

    @Test
    public void createOrder() {
        System.out.println("orderService:" + orderService);
        Order order = new Order();
        order.setOrderId(2L);
        order.setOrderNo("MT0000011");
        order.setProductName("超值优惠红烧肉套餐");
        order.setPurchaseName("张三");
        boolean ret = orderService.createOrder(order, true);
        boolean ret1 = orderService.createOrder(order, false);
        Assert.assertTrue(ret);
        Assert.assertTrue(ret1);

    }

    @Test
    public void updateOrder() {
        Order order = new Order();
        order.setOrderNo("MT0000011");
        order.setProductName("超值优惠红烧肉套餐");
        order.setPurchaseName("张三");
        boolean ret = orderService.update(1L, order);

    }

}
