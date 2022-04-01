package org.wmc.integrated.bizlog.service.impl;

import org.wmc.integrated.bizlog.bean.Order;
import org.wmc.integrated.bizlog.constants.LogRecordType;
import org.wmc.integrated.bizlog.context.LogRecordContext;
import org.wmc.integrated.bizlog.service.IOrderService;
import com.mzt.logapi.starter.annotation.LogRecordAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    /*'张三下了一个订单,购买商品「超值优惠红烧肉套餐」,下单结果:true' */
    @Override
    @LogRecordAnnotation(
            fail = "创建订单失败，失败原因：「{{#_errorMsg}}」",
            category = "MANAGER_VIEW",
            detail = "{{#order.toString()}}",
            operator = "{{#currentUser}}",
            success = "{{#disable ? '停用' : '启用'}}{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{ORDER{#order.getOrderId()}}」,下单结果:{{#_ret}}",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}")
    public boolean createOrder(Order order, boolean disable) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);
        return true;
    }

    @Override
    @LogRecordAnnotation(success = "更新了订单{ORDER{#orderId}},更新内容为...",
            prefix = LogRecordType.ORDER, bizNo = "{{#order.orderNo}}",
            detail = "{{#order.toString()}}")
    public boolean update(Long orderId, Order order) {
        return false;
    }
}