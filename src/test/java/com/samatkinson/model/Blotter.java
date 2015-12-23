package com.samatkinson.model;

public class Blotter {
    private static int noOfStock = 0;
    private static int position = 0;

    public void add(Trade trade) {
        if(trade.direction.equals("buy")){
            noOfStock += trade.totalFilled;
            trade.fills.forEach(t -> position += (t.qty * t.price));
        }
    }

    public int profit(int bid, int ask){
        return (noOfStock * bid) - position;
    }
}
