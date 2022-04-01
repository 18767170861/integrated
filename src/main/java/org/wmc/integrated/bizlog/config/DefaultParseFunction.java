package org.wmc.integrated.bizlog.config;

import org.wmc.integrated.bizlog.bean.Order;
import org.wmc.integrated.bizlog.service.OrderQueryService;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * IParseFunction 自定义转换函数的接口，可以实现IParseFunction 实现对LogRecord注解中使用的函数扩展 例子：
 */
@Component
public class DefaultParseFunction implements IParseFunction {

    @Autowired
    @Lazy //为了避免类加载顺序的问题 最好为Lazy，没有问题也可以不加
    private OrderQueryService orderQueryService;

    // 函数名称为 ORDER
    @Override
    public String functionName() {
        return "ORDER";
    }

    @Override
    //这里的 value 可以吧 Order 的JSON对象的传递过来，然后反解析拼接一个定制的操作日志内容
    public String apply(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }
        Order order = orderQueryService.queryOrder(Long.parseLong(value));
        //把订单产品名称加上便于理解，加上 ID 便于查问题
        return order.getProductName().concat("(").concat(value).concat(")");
    }
}