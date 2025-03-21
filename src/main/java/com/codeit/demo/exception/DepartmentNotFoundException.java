package com.codeit.demo.exception;

public class DepartmentNotFoundException extends RuntimeException {
    
    public DepartmentNotFoundException(String message) {
        super(message);
    }
    
    public DepartmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}