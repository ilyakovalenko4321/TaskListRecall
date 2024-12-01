package com.ilyaKovalenko.SelfWritedTaskList.domain.Exception;

public class IncorrectSecretKeyException extends RuntimeException {
    public IncorrectSecretKeyException(String message) {
        super(message);
    }
}
