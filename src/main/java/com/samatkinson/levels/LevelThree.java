package com.samatkinson.levels;

import com.samatkinson.api.StockfighterAPI;
import com.samatkinson.model.Quote;
import com.samatkinson.model.Trade;

public class LevelThree {
    private static final String symbol = "IXSU";
    private static String account = "SS58566137";
//    private static Blotter blotter;

    public static void main(String[] args) {
        StockfighterAPI stockfighterAPI = new StockfighterAPI("https://api.stockfighter.io", "YNYEX");
        Quote quote = stockfighterAPI.stockQuote(symbol);

        int ask = quote.ask;
        int bid = quote.bid;

        Trade trade = stockfighterAPI.trade(1000, bid - 5, account, symbol, Trade.LIMIT);

//        blotter.add(trade);


    }

}
