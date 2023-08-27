package com.iyzico.challenge.exception;


public class NotFoundException extends Exception {
    public NotFoundException(){
        super("record not found");
    }
}
