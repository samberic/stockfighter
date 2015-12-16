package com.samatkinson.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.samatkinson.error.StockfighterException;
import com.samatkinson.levels.LevelTwo;
import com.samatkinson.model.Trade;
import com.sun.tools.corba.se.idl.StringGen;

import java.io.IOException;

import static com.mashape.unirest.http.Unirest.post;

public class StockfighterAPI {
    public String bookTradeUrl = "/ob/api/venues/%s/stocks/%s/orders";
    private String authKey = "276a779958d461a4724220bf4281f4dd3c4f6d01";

    private String server;
    private String symbol;
    private String exchange;
    private String account;

    public StockfighterAPI(String server, String symbol, String exchange, String account) {
        this.server = server;
        this.symbol = symbol;
        this.exchange = exchange;
        this.account = account;
    }

    public Trade trade(int filledTradeCount, int price) {
        try {
            String format = server + String.format(bookTradeUrl, symbol, exchange);

            System.out.println("Booking to url " + format);
            HttpResponse<String> textresp = post(format)
                    .header("X-Starfighter-Authorization", authKey)
                    .body(new JsonNode("{" +
                            "\"account\": \"" + account + "\",\n" +
                            "\"price\": " + price + ",\n" +
                            "\"qty\": " + filledTradeCount + ",\n" +
                            "\"direction\": \"buy\",\n" +
                            "\"orderType\": \"market\"\n" +
                            "}"))
                    .asString();

            System.out.println(textresp.getBody());

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


            ObjectMapper objectMapper = new ObjectMapper();
            Trade trade = objectMapper.readValue(jsonNodeHttpResponse.getBody().getObject().toString(), Trade.class);

            return trade;

        } catch (UnirestException | IOException e) {
            throw new StockfighterException("Error in post request", e);

        }

    }
}
