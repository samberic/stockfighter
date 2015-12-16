package com.samatkinson.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.samatkinson.error.StockfighterException;
import com.samatkinson.model.OrderBook;
import com.samatkinson.model.Trade;

import java.io.IOException;

import static com.mashape.unirest.http.Unirest.*;

public class StockfighterAPI {
    public String bookTradeUrl = "/ob/api/venues/%s/stocks/%s/orders";
    public String orderBookUrl = "/ob/api/venues/%s/stocks/%s";

    private String authKey = "276a779958d461a4724220bf4281f4dd3c4f6d01";

    private String server;
    private String symbol;
    private String venue;

    private ObjectMapper objectMapper = new ObjectMapper();


    public StockfighterAPI(String server, String symbol, String venue) {
        this.server = server;
        this.symbol = symbol;
        this.venue = venue;
    }

    public Trade trade(int filledTradeCount, int price, String account) {
        try {
            String format = server + String.format(bookTradeUrl, symbol, venue);

            HttpResponse<JsonNode> jsonNodeHttpResponse = post(format)
                    .header("X-Starfighter-Authorization", authKey)
                    .body(new JsonNode("{" +
                            "\"account\": \"" + account + "\"," +
                            "\"price\": " + price + "," +
                            "\"qty\": " + filledTradeCount + "," +
                            "\"direction\": \"buy\"," +
                            "\"orderType\": \"market\"" +
                            "}"))
                    .asJson();


            Trade trade = objectMapper.readValue(getJsonContent(jsonNodeHttpResponse), Trade.class);

            return trade;

        } catch (UnirestException | IOException e) {
            throw new StockfighterException("Error in post request", e);

        }

    }

    private String getJsonContent(HttpResponse<JsonNode> jsonNodeHttpResponse) {
        return jsonNodeHttpResponse.getBody().getObject().toString();
    }

    public OrderBook orderBook() {
        String format = server + String.format(orderBookUrl, venue, symbol);
        HttpResponse<JsonNode> jsonNodeHttpResponse = null;
        try {
            System.out.println(format);
            HttpResponse<String> x = get(format)
                    .header("X-Starfighter-Authorization", authKey)
                    .asString();
            System.out.println(x.getBody());

            jsonNodeHttpResponse = get(format)
                    .header("X-Starfighter-Authorization", authKey).asJson();
            return objectMapper.readValue(getJsonContent(jsonNodeHttpResponse), OrderBook.class);

        } catch (UnirestException | IOException e) {
            throw new StockfighterException("Error in order book request", e);

        }

    }
}
