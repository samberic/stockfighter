package com.samatkinson.model;

import java.util.List;

public class Trade {
    public final List<Fill> fills;
    public final String orderType;

    public Trade(String orderType, List<Fill> fills) {
        this.orderType = orderType;
        this.fills = fills;
    }
}
