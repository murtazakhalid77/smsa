package com.smsa.backend.Exception;

import org.apache.poi.ss.formula.functions.T;

import java.util.function.Supplier;

public class RecordNotFoundException extends RuntimeException {
    public  RecordNotFoundException(String message){

        super(message);
    }



}
