package com.kertas_kerja.data_kinerja.exception;

public class UnauthenticationException extends RuntimeException {

    public UnauthenticationException(String message) {
        super(message);
    }

    public UnauthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}