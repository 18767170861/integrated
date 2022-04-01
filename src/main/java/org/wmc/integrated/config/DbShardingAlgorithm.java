package org.wmc.integrated.config;

import org.wmc.integrated.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

@Slf4j
public class DbShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        // collection:["m1","m2"],preciseShardingValue:{"logicTableName":"course","columnName":"user_id","value":0}
        log.info("DbShardingAlgorithm collection:" + JsonUtils.serialize(availableTargetNames) + ",preciseShardingValue:" + JsonUtils.serialize(shardingValue));
        for (String name : availableTargetNames) {
            //订单号取模加1 与 订单表t_order_1 和 t_order_2的尾号做比对，如相等，就直接返回t_order_1 或 t_order_2
            if (name.endsWith(String.valueOf(shardingValue.getValue() % 2 + 1))) {
                log.info("DbShardingAlgorithm return name: " + name);
                return "m3";
            }
        }
        return null;
    }
}