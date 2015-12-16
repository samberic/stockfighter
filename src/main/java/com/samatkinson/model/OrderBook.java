package com.samatkinson.model;

import java.util.List;

public class OrderBook {
    public boolean ok;
    public String venue;
    public String symbol;
    public String ts;
    public List<Quote> bids;
    public List<Quote> asks;


    public OrderBook(){}

    public static class Quote {
        public int price;
        public int qty;
        public boolean isBuy;

        public Quote(){}
    }
}
