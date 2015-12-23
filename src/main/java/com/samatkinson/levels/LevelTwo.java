package com.samatkinson.levels;

import com.samatkinson.api.StockfighterAPI;
import com.samatkinson.model.Trade;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LevelTwo {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() ->
                new StockfighterAPI("https://api.stockfighter.io", "BTI"
                ).
                        trade(2000, 6000, "RTB59046910", "CURHEX", Trade.LIMIT), 0, 1, TimeUnit.SECONDS);
    }


}
