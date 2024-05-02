package com.codigo.domain.agregates.exception;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String msgError){
        super(msgError);
    }
}
