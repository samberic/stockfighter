package com.samatkinson.model;

public class PnLCalculator {
    private final String stock;
    private final int price;

    public PnLCalculator(String stock, int price) {
        this.stock = stock;
        this.price = price;
    }

    public int report(Position position) {
        return (price - position.averagePrice()) * position.size();
    }
}
