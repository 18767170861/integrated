package org.wmc.integrated.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_udict")
public class Udict {
    @TableId(type = IdType.AUTO)
    private Long dictid;
    private String ustatus;
    private String uvalue;
}