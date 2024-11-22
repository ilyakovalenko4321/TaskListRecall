package com.ilyaKovalenko.SelfWritedTaskList.domain.Exception;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String message) {
        super(message);
    }
}
