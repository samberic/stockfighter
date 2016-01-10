package com.samatkinson.model;

public class Position {
    private int averagePrice;
    private int qty;

    public String symbol() {
        return "ABC";
    }

    public int size() {
        return qty;
    }

    public void update(int price, int qty) {
        averagePrice = price;
        this.qty = qty;
    }

    public int money() {
        return averagePrice * qty;
    }

    public int averagePrice() {
        return averagePrice;
    }

}
