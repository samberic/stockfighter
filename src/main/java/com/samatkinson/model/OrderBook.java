package com.samatkinson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBook {
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
