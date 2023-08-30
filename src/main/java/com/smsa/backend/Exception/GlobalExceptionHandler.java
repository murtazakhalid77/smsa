package com.smsa.backend.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value =RecordNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceRecordNotFOundException(RecordNotFoundException ex){
         ErrorMessage errorMessage = ErrorMessage.builder()
                .body(ex.getMessage()).build();
        return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value =RecordAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> resourceRecordalreadyExistException(RecordAlreadyExistException ex){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .body(ex.getMessage()).build();
        return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = SheetAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> sheetAlreadyExistException(SheetAlreadyExistException ex){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .body(ex.getMessage()).build();
        return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = ParsingExcelException.class)
    public ResponseEntity<ErrorMessage> parsingExcelException(ParsingExcelException ex){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .body(ex.getMessage()).build();
        return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = ExcelMakingException.class)
    public ResponseEntity<ErrorMessage> excelMakingException(ExcelMakingException ex){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .body(ex.getMessage()).build();
        return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AwbDublicateException.class)
    public ResponseEntity<?> awbDublicateException(AwbDublicateException ex){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .body(ex.getMessage()).build();
        return new ResponseEntity(errorMessage,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = SalesReportException.class)
    public ResponseEntity<ErrorMessage> salesReportException(SalesReportException ex){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .body(ex.getMessage()).build();
        return new ResponseEntity(errorMessage, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(value = InvalidEmailException.class)
    public ResponseEntity<ErrorMessage> invalidEmailException(InvalidEmailException ex){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .body(ex.getMessage()).build();
        return new ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }

}

