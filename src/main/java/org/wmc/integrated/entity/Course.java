package org.wmc.integrated.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Course {
    @TableId(type = IdType.AUTO)
    private Long cid;
    private String cname;
    private Long userId;
    private String cstatus;
}