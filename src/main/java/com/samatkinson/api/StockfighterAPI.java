package com.samatkinson.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.samatkinson.error.StockfighterException;
import com.samatkinson.model.OrderBook;
import com.samatkinson.model.Quote;
import com.samatkinson.model.Symbol;
import com.samatkinson.model.Trade;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mashape.unirest.http.Unirest.*;

public class StockfighterAPI {
    public String bookTradeUrl = "/ob/api/venues/%s/stocks/%s/orders";
    public String orderBookUrl = "/ob/api/venues/%s/stocks/%s";
    public String stocksUrl = "/ob/api/venues/%s/stocks";
    public String stockQuoteUrl = "/ob/api/venues/%s/stocks/%s/quote";
    private String orderStatusUrl = "/ob/api/venues/%s/stocks/%s/orders/%s";
    private String cancelUrl = "/ob/api/venues/%s/stocks/%s/orders/%s";

    private String authKey = "276a779958d461a4724220bf4281f4dd3c4f6d01";
    private String server;
    private String venue;
    private ObjectMapper objectMapper = new ObjectMapper();


    public StockfighterAPI(String server, String venue) {
        this.server = server;
        this.venue = venue;
    }

    public Trade trade(int filledTradeCount, int price, String account, Object symbol, String orderType) {
        try {
            String format = server + String.format(bookTradeUrl, venue, symbol);
            HttpResponse<String> s = post(format)
                    .header("X-Starfighter-Authorization", authKey)
                    .body(new JsonNode("{" +
                            "\"account\": \"" + account + "\"," +
                            "\"price\": " + price + "," +
                            "\"qty\": " + filledTradeCount + "," +
                            "\"direction\": \"buy\"," +
                            "\"orderType\": \"" + orderType + "\"" +
                            "}"))
                    .asString();

            System.out.println(s.getBody());

            HttpResponse<JsonNode> jsonNodeHttpResponse = post(format)
                    .header("X-Starfighter-Authorization", authKey)
                    .body(new JsonNode("{" +
                            "\"account\": \"" + account + "\"," +
                            "\"price\": " + price + "," +
                            "\"qty\": " + filledTradeCount + "," +
                            "\"direction\": \"buy\"," +
                            "\"orderType\": \"" + orderType + "\"" +
                            "}"))
                    .asJson();


            Trade trade = objectMapper.readValue(getJsonContent(jsonNodeHttpResponse), Trade.class);

            return trade;

        } catch (UnirestException | IOException e) {
            throw new StockfighterException("Error in post request", e);

        }

    }

    public OrderBook orderBook(String symbol) {
        String format = server + String.format(orderBookUrl, venue, symbol);
        HttpResponse<JsonNode> jsonNodeHttpResponse = null;
        try {
            jsonNodeHttpResponse = get(format)
                    .header("X-Starfighter-Authorization", authKey).asJson();
            return objectMapper.readValue(getJsonContent(jsonNodeHttpResponse), OrderBook.class);

        } catch (UnirestException | IOException e) {
            throw new StockfighterException("Error in order book request", e);
        }
    }

    public List<Symbol> symbols() {
        String formattedUrl = server + String.format(stocksUrl, venue);
        HttpResponse<JsonNode> jsonNodeHttpResponse = null;
        try {
            jsonNodeHttpResponse = get(formattedUrl)
                    .header("X-Starfighter-Authorization", authKey).asJson();

            JSONArray stocks = jsonNodeHttpResponse.getBody().getObject().getJSONArray("symbols");

            List<Symbol> result = new ArrayList<>();
            for(int i=0; i<stocks.length(); i++) {
                result.add(objectMapper.readValue(stocks.getJSONObject(i).toString(), Symbol.class));
            }

            return result;

        } catch (UnirestException | IOException e) {
            throw new StockfighterException("Error in order book request", e);
        }
    }

    private String getJsonContent(HttpResponse<JsonNode> jsonNodeHttpResponse) {
        return jsonNodeHttpResponse.getBody().getObject().toString();
    }

    public Quote stockQuote(String symbol) {
        String format = server + String.format(stockQuoteUrl, venue, symbol);
        HttpResponse<JsonNode> jsonNodeHttpResponse = null;
        try {
            jsonNodeHttpResponse = get(format)
                    .header("X-Starfighter-Authorization", authKey).asJson();
            return objectMapper.readValue(getJsonContent(jsonNodeHttpResponse), Quote.class);

        } catch (UnirestException | IOException e) {
            throw new StockfighterException("Error in quote requestt", e);
        }
    }

    public Trade orderStatus(int orderID, String symbol) {
        String format = server + String.format(orderStatusUrl, venue, symbol, orderID);
        HttpResponse<JsonNode> jsonNodeHttpResponse = null;
        try {
            jsonNodeHttpResponse = get(format)
                    .header("X-Starfighter-Authorization", authKey).asJson();
            return objectMapper.readValue(getJsonContent(jsonNodeHttpResponse), Trade.class);
        } catch (UnirestException | IOException e) {
            throw new StockfighterException("Error in quote requestt", e);
        }
    }

    public Trade cancelOrder(int orderID, String symbol) {
        String format = server + String.format(cancelUrl, venue, symbol, orderID);
        try {
            StockfighterResponse stockfighterResponse = new StockfighterResponse(delete(format)
                    .header("X-Starfighter-Authorization", authKey).asJson());

            if(stockfighterResponse.getBoolean("ok")){
                return stockfighterResponse.as(Trade.class);
            }else{
                throw new StockfighterException(stockfighterResponse.getString("error"));
            }
        } catch (UnirestException e) {
            throw new StockfighterException("Error in quote requestt", e);
        }
    }

}
