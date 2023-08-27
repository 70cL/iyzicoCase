package com.iyzico.challenge.exception;


public class SeatTakenException extends Exception {
    public SeatTakenException(){
        super("Seat already sold!");
    }

}
