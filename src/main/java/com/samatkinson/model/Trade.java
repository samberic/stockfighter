package com.samatkinson.model;

import java.util.List;

public class Trade {
    public List<Fill> fills;
    public String orderType;
    public boolean ok;
    public String symbol;
    public String venue;
    public String direction;
    public int originalQty;
    public int qty;
    public int price;
    public int id;
    public String account;
    public String ts;
    public int totalFilled;
    public boolean open;

    public Trade(){}

    public Trade(String orderType,
                 List<Fill> fills,
                 boolean ok,
                 String symbol,
                 String venue,
                 String direction,
                 int originalQty,
                 int qty,
                 int price,
                 int id,
                 String account,
                 String ts,
                 int totalFilled,
                 boolean open) {
        this.orderType = orderType;
        this.fills = fills;
        this.ok = ok;
        this.symbol = symbol;
        this.venue = venue;
        this.direction = direction;
        this.originalQty = originalQty;
        this.qty = qty;
        this.price = price;
        this.id = id;
        this.account = account;
        this.ts = ts;
        this.totalFilled = totalFilled;
        this.open = open;
    }
}
