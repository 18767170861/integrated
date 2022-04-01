package org.wmc.integrated.bizlog.service;


import org.wmc.integrated.bizlog.bean.Order;

public interface IOrderService {
    boolean createOrder(Order order, boolean disable);

    boolean update(Long orderId, Order order);
}