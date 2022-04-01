package org.wmc.integrated.bizlog.function;

import org.wmc.integrated.bizlog.bean.Order;
import org.wmc.integrated.bizlog.service.OrderQueryService;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Component
public class OrderParseFunction implements IParseFunction {
    @Resource
    @Lazy
    private OrderQueryService orderQueryService;

    @Override
    public String functionName() {
        return "ORDER";
    }

    @Override
    public String apply(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        Order order = orderQueryService.queryOrder(Long.parseLong(value));
        return order.getProductName().concat("(").concat(value).concat(")");
    }
}