package com.elasticsearchutil.demo.controller;

import com.elasticsearchutil.demo.entity.ExampleResult;
import com.elasticsearchutil.demo.entity.Student;
import com.elasticsearchutil.demo.service.ExampleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/example")
public class ExampleController {

    private final ExampleService exampleService;

    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @RequestMapping(value = "insert")
    public ExampleResult insert(@RequestBody List<Student> studentList) {
        return exampleService.insert(studentList);
    }

    @RequestMapping(value = "findByRemark")
    public ExampleResult findByRemark(@RequestParam("remark") String remark) {
        return exampleService.findByRemark(remark);
    }

    @RequestMapping(value = "findBySex")
    public ExampleResult findBySex(@RequestParam("sex") Integer sex) {
        return exampleService.findBySex(sex);
    }

    @RequestMapping(value = "deleteById")
    public ExampleResult deleteById(@RequestParam("id") String id) {
        return exampleService.deleteById(id);
    }

    @RequestMapping(value = "deleteIndex")
    public ExampleResult deleteIndex() {
        return exampleService.deleteIndex();
    }
}
