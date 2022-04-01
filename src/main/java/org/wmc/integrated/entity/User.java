package org.wmc.integrated.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")    //指定对应的表名
public class User {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String username;
    private String ustatus;
}