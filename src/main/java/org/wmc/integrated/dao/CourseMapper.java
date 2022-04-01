package org.wmc.integrated.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.wmc.integrated.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {

    @Select({"SELECT a.* from course a JOIN t_udict b ON a.cid=b.cid where a.cid=#{cid} and b.cid=#{cid}"})
    List<Course> selectByJoin(@Param("cid") Long cid);
}