package com.samatkinson.levels;

import com.samatkinson.api.StockfighterAPI;
import com.samatkinson.model.OrderBook;
import com.samatkinson.model.Trade;

public class LevelThree {
    private static final String symbol = "ARCC";
    private static String account = "DFB4908077";
    private static int noOfStock = 0;

    public static void main(String[] args) {
        StockfighterAPI stockfighterAPI = new StockfighterAPI("https://api.stockfighter.io", "DWMEX");

        OrderBook orderBook = stockfighterAPI.orderBook(symbol);

        int ask = orderBook.asks.get(0).price;
        int bid = orderBook.bids.get(0).price;

        while (true) {

            Trade buyTrade = stockfighterAPI.trade(1000 - noOfStock, bid - 1, account, symbol);
            noOfStock = buyTrade.totalFilled;


            Trade sellTrade = stockfighterAPI.trade(noOfStock, ask + 1, account, symbol);
            noOfStock -= sellTrade.totalFilled;
        }




    }
}
