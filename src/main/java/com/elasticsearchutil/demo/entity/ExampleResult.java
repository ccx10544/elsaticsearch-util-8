package com.elasticsearchutil.demo.entity;

import lombok.Data;

import java.util.List;

@Data
public class ExampleResult {

    private Boolean success;

    private String message;

    private String code;

    private Object data;

    private Long total;

    private List<?> list;

    public static ExampleResult success() {
        ExampleResult result = new ExampleResult();
        result.setSuccess(true);
        return result;
    }

    public static ExampleResult success(Object data) {
        ExampleResult result = new ExampleResult();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> ExampleResult success(List<T> list, Long total) {
        ExampleResult result = new ExampleResult();
        result.setSuccess(true);
        result.setList(list);
        result.setTotal(total);
        return result;
    }

    public static ExampleResult fail(String message, String code) {
        ExampleResult result = new ExampleResult();
        result.setSuccess(false);
        result.setMessage(message);
        result.setCode(code);
        return result;
    }
}
