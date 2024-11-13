package com.ilyaKovalenko.SelfWritedTaskList.domain.Exception;

public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException(String message) {
    super(message);
  }
}
