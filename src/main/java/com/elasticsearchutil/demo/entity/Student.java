package com.elasticsearchutil.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Student implements Serializable {

    private String id;

    private String name;

    private Integer age;

    private Integer sex;

    private Date birthday;

    private String remark;
}
