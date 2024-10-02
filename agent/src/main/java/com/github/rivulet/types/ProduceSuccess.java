package com.github.rivulet.types;

public class ProduceSuccess {
    public String response;
    public Integer statusCode;

    public ProduceSuccess(String response, Integer statusCode) {
        this.response=response;
        this.statusCode=statusCode;
    }
}
