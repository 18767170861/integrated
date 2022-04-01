package org.wmc.integrated.bizlog.config;

import com.mzt.logapi.beans.Operator;
import com.mzt.logapi.service.IOperatorGetService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 重写OperatorGetServiceImpl通过上下文获取用户的扩展
 */
@Component
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    @Override
    public Operator getUser() {
        // return Optional.ofNullable(UserUtils.getUser())
        //                .map(a -> new Operator(a.getName(), a.getLogin()))
        //                .orElseThrow(()->new IllegalArgumentException("user is null"));
        Operator operator = new Operator();
        operator.setOperatorId("hahaha");
        return operator;
    }
}
