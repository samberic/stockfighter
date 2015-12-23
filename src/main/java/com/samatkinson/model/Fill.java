package com.samatkinson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fill {
    public int qty;
    public int price;
    public String ts;

    public Fill(){}

    public Fill(int qty, int price, String timestamp) {

        this.qty = qty;
        this.price = price;
        this.ts = timestamp;
    }
}
