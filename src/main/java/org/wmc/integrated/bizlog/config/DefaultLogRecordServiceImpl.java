package org.wmc.integrated.bizlog.config;


import org.wmc.integrated.mongo.repository.LogRecordRepository;
import org.wmc.integrated.util.JsonUtils;
import com.google.common.collect.Lists;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * ILogRecordService 保存/查询日志的例子,使用者可以根据数据量保存到合适的存储介质上，比如保存在数据库/或者ES。自己实现保存和删除就可以了
 */
@Slf4j
@Component
public class DefaultLogRecordServiceImpl implements ILogRecordService {

    @Autowired
    private LogRecordRepository logRecordRepository;

    public void record(LogRecord logRecord) {
        logRecord.setId(new Random().nextInt());
        log.info("【logRecord】log={}", JsonUtils.serialize(logRecord));
        org.wmc.integrated.bizlog.bean.LogRecord record = org.wmc.integrated.bizlog.bean.LogRecord.builder().build();

        BeanUtils.copyProperties(logRecord, record);
        log.info("【logRecord copy】log={}", JsonUtils.serialize(logRecord));
        logRecordRepository.insert(record);
    }

    @Override
    public List<LogRecord> queryLog(String bizKey) {
        return Lists.newArrayList();
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo) {
        return Lists.newArrayList();
    }
}
