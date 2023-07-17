package com.smsa.backend.Exception;

public class RecordAlreadyExistException extends RuntimeException{
    public RecordAlreadyExistException(String message){
        super(message);
    }

}
