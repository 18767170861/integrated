package org.wmc.integrated.bizlog.service.impl;

import org.wmc.integrated.bizlog.bean.Order;
import org.wmc.integrated.bizlog.service.OrderQueryService;
import org.springframework.stereotype.Service;


@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    @Override
    public Order queryOrder(long parseLong) {
        Order order = new Order();
        order.setOrderId(parseLong);
        order.setOrderNo("test001");
        order.setProductName("全球通黄金会员");
        order.setPurchaseName("小白");
        return order;
    }
}
