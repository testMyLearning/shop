package com.oz.common.exception;

public class CustomException  extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
    public CustomException(String msg, Throwable t){
        super(msg,t);
    }
}
