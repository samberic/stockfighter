package com.samatkinson.model;

import org.junit.Test;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static com.samatkinson.makers.TradeMaker.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlotterTest {

    private final int numberOwned = 200;
    private int currentPrice = 107;
    private int purchasePrice = 100;

    @Test
    public void calculatesProfitCorrectlyForSingleStock() throws Exception {
        Blotter blotter = new Blotter();
        blotter.add(aTrade());

        assertThat(blotter.positions().size(), is(1));
        Position position = blotter.positions().get(0);
        assertThat(position.symbol(), is("ABC"));
        assertThat(position.size(), is(numberOwned));
        assertThat(position.money(), is(purchasePrice * numberOwned));
        assertThat(new PnLCalculator("ABC", 107).report(position), is((currentPrice - purchasePrice) * numberOwned));
    }

    private Trade aTrade() {
        return make(a(Trade,
            with(symbol, "ABC"),
            with(direction, "buy"),
            with(fills, asList(new Fill(numberOwned, purchasePrice, "timestamp")))
            ));
    }


}
