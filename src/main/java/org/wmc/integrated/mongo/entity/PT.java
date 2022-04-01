package org.wmc.integrated.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "pt")
public class PT implements Serializable {

    @Id
    private String id;

    /**
     * 学生id
     **/
    @Field("student_Id")
    private String student_id;

    /**
     * 学生姓名
     **/
    private String student_name;

    /**
     * 学校id
     **/
    private String school_id;

    /**
     * 学校名称
     **/
    private String school_name;

    /**
     * 年级id
     **/
    private String grade_id;

    /**
     * 年级名称
     **/
    private String grade_name;

    /**
     * 班级id
     **/
    private String class_id;

    /**
     * 班级名称
     **/
    private String class_name;

    /**
     * 批次
     **/
    private String batch;

    /**
     * 测试时间
     **/
    private Date test_time;

    /**
     * 项目
     **/
    private List<Item> items;
}