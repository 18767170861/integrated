package org.wmc.integrated.bizlog.service;


import org.wmc.integrated.bizlog.bean.Order;

public interface OrderQueryService {
    Order queryOrder(long parseLong);
}
