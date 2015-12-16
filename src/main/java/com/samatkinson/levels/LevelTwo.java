package com.samatkinson.levels;

import com.samatkinson.api.StockfighterAPI;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LevelTwo {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() ->
                new StockfighterAPI("https://api.stockfighter.io","CURHEX", "BTI"
                ).
                        trade(2000, 6000, "RTB59046910"), 0, 1, TimeUnit.SECONDS);
    }


}
