package com.smsa.backend.Exception;

public class RecordNotSaveException extends RuntimeException {
    public RecordNotSaveException(String message){
        super(message);
    }
}
