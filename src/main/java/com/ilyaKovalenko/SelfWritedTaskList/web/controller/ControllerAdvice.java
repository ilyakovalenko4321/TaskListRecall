package com.ilyaKovalenko.SelfWritedTaskList.web.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public String handler(Exception e){
        e.printStackTrace();
        return e.getMessage();
    }

}
