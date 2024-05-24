package com.elasticsearchutil.demo.service;

import com.elasticsearchutil.demo.entity.ExampleResult;
import com.elasticsearchutil.demo.entity.Student;

import java.util.List;

public interface ExampleService {

    /**
     * 插入数据
     *
     * @param studentList 数据
     * @return result
     */
    ExampleResult insert(List<Student> studentList);

    /**
     * 根据remark字段查询
     *
     * @param remark remark
     * @return result
     */
    ExampleResult findByRemark(String remark);

    /**
     * 根据sex字段查询
     *
     * @param sex 性别
     * @return result
     */
    ExampleResult findBySex(Integer sex);

    /**
     * 根据id删除文档内容
     *
     * @param id id
     * @return result
     */
    ExampleResult deleteById(String id);

    /**
     * 删除索引
     *
     * @return result
     */
    ExampleResult deleteIndex();
}
