package com.samatkinson.model;

import java.util.LinkedList;
import java.util.List;

public class Blotter {
    List<Position> positions = new LinkedList<>();

    public void add(Trade trade) {
        Position first = positions.stream().filter(
            p -> p.symbol().equals(trade.symbol))
            .findFirst()
            .orElseGet(() -> {
                Position e = new Position();
                positions.add(e);
                return e;
            });

        trade.fills.forEach(fill -> first.update(fill.price, fill.qty));

    }

    public List<Position> positions() {
        return positions;
    }
}
