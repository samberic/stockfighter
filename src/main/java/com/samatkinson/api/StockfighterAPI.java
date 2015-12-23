package com.samatkinson.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.samatkinson.model.OrderBook;
import com.samatkinson.model.Quote;
import com.samatkinson.model.Symbol;
import com.samatkinson.model.Trade;

import java.util.List;

import static com.mashape.unirest.http.Unirest.*;

public class StockfighterAPI {
    public String bookTradeUrl = "/ob/api/venues/%s/stocks/%s/orders";
    public String orderBookUrl = "/ob/api/venues/%s/stocks/%s";
    public String stocksUrl = "/ob/api/venues/%s/stocks";
    public String stockQuoteUrl = "/ob/api/venues/%s/stocks/%s/quote";
    private String orderStatusUrl = "/ob/api/venues/%s/stocks/%s/orders/%s";
    private String cancelUrl = "/ob/api/venues/%s/stocks/%s/orders/%s";

    private String server;
    private String venue;


    public StockfighterAPI(String server, String venue) {
        this.server = server;
        this.venue = venue;
    }

    public Trade trade(int filledTradeCount, int price, String account, Object symbol, String orderType) {
            String format = server + String.format(bookTradeUrl, venue, symbol);

            HttpRequestWithBody post = post(format);
            post.body(new JsonNode("{" +
                    "\"account\": \"" + account + "\"," +
                    "\"price\": " + price + "," +
                    "\"qty\": " + filledTradeCount + "," +
                    "\"direction\": \"buy\"," +
                    "\"orderType\": \"" + orderType + "\"" +
                    "}"));

            return make(post).as(Trade.class);

    }

    public OrderBook orderBook(String symbol) {
        String format = server + String.format(orderBookUrl, venue, symbol);
        return make(get(format)).as(OrderBook.class);
    }

    public List<Symbol> symbols() {
        String format = server + String.format(stocksUrl, venue);
        return make(get(format)).asListOf(Symbol.class, "symbols");


    }

    public Quote stockQuote(String symbol) {
        String format = server + String.format(stockQuoteUrl, venue, symbol);

        return make(get(format)).as(Quote.class);

    }

    public Trade orderStatus(int orderID, String symbol) {
        String format = server + String.format(orderStatusUrl, venue, symbol, orderID);

        return make(get(format)).as(Trade.class);
    }

    public Trade cancelOrder(int orderID, String symbol) {
        String format = server + String.format(cancelUrl, venue, symbol, orderID);

        return make(delete(format)).as(Trade.class);
    }

    private StockfighterResponse make(HttpRequest request) {
        StockfighterResponse response = new StockfighterResponse(request);
        response.execute();
        return response;
    }

}
