package com.samatkinson.error;


public class StockfighterException extends RuntimeException{
    public StockfighterException(String error, Exception e) {
        super(error, e);
    }
    public StockfighterException(String error) {
        super(error);
    }
}
