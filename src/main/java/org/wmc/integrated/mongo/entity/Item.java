package org.wmc.integrated.mongo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Item implements Serializable {

    /**
     * 项目名称
     **/
    private String item_name;

    /**
     * 分数
     **/
    private double score;

    /**
     * 单位
     **/
    private String unit;
}