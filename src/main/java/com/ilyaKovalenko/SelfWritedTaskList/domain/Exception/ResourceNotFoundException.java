package com.ilyaKovalenko.SelfWritedTaskList.domain.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
