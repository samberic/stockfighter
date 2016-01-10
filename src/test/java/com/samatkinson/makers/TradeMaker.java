package com.samatkinson.makers;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import com.samatkinson.model.Fill;
import com.samatkinson.model.Trade;

import java.util.List;

import static java.util.Arrays.asList;

public class TradeMaker {


    public static Property<Trade, String> orderType = new Property<>();
    public static Property<Trade, List<Fill>> fills = new Property<>();
    public static Property<Trade, String> symbol = new Property<>();
    public static Property<Trade, String> venue = new Property<>();
    public static Property<Trade, String> direction = new Property<>();
    public static Property<Trade, Integer> originalQty = new Property<>();
    public static Property<Trade, Integer> qty = new Property<>();
    public static Property<Trade, Integer> price = new Property<>();
    public static Property<Trade, Integer> id = new Property<>();
    public static Property<Trade, String> account = new Property<>();
    public static Property<Trade, String> ts = new Property<>();
    public static Property<Trade, Integer> totalFilled = new Property<>();
    public static Property<Trade, Boolean> open = new Property<>();

    public static final Instantiator<Trade> Trade = lookup -> {
        Trade trade = new Trade(
                lookup.valueOf(orderType, "fillorbust"),
                lookup.valueOf(fills, asList(new Fill(20, 200, ""))),
                lookup.valueOf(symbol, "GBR"),
                lookup.valueOf(venue, "ESSEX"),
                lookup.valueOf(direction, "buy"),
                lookup.valueOf(originalQty, 20),
                lookup.valueOf(qty, 20),
                lookup.valueOf(price, 200),
                lookup.valueOf(id, 1234),
                lookup.valueOf(account, "ABCXYZ"),
                lookup.valueOf(ts, "12d"),
                lookup.valueOf(totalFilled, 20),
                lookup.valueOf(open, false)
        );
        return trade;
    };
}
