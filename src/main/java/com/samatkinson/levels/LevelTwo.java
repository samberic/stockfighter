package com.samatkinson.levels;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.samatkinson.error.StockfighterException;
import com.samatkinson.model.Fill;
import com.samatkinson.model.Trade;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.mashape.unirest.http.Unirest.post;

public class LevelTwo {
    private final String symbol;
    private final String exchange;
    private final String account
            ;
    private String bookTradeUrl = "https://api.stockfighter.io/ob/api/venues/%s/stocks/%s/orders";
    private String authKey = "276a779958d461a4724220bf4281f4dd3c4f6d01";

    public static void main(String[] args) {
        LevelTwo levelTwo = new LevelTwo("CURHEX", "BTI", "RTB59046910");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> levelTwo.trade(2000), 0, 1, TimeUnit.SECONDS);
    }

    public LevelTwo(String symbol, String exchange, String account) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.account = account;
    }

        public Trade trade(int filledTradeCount){
        try {
            String format = String.format(bookTradeUrl, symbol, exchange);

            System.out.println("Booking to url " + format);
            HttpResponse<JsonNode> jsonNodeHttpResponse = post(format)
                    .header("X-Starfighter-Authorization", authKey)
                    .body(new JsonNode("{" +
                            "    \"account\": \"" + account + "\",\n" +
                            "    \"price\": 6100,\n" +
                            "    \"qty\": " + filledTradeCount + ",\n" +
                            "    \"direction\": \"buy\",\n" +
                            "    \"orderType\": \"market\"\n" +
                            "}")).asJson();

            return new Trade(extractOrderTye(jsonNodeHttpResponse),
                    extractFillList(jsonNodeHttpResponse)
            );

        } catch (UnirestException e) {
            throw new StockfighterException("Error in post request", e);
        }

    }

    private List<Fill> extractFillList(HttpResponse<JsonNode> jsonNodeHttpResponse) {
        List<Fill> fillsList = new ArrayList<>();
        JSONArray fills = jsonNodeHttpResponse.getBody().getObject().getJSONArray("fills");
        for (int i = 0; i < fills.length(); i++) {
            JSONObject jsonObject = fills.getJSONObject(i);
            fillsList.add(new Fill(jsonObject.getInt("qty")));
        }
        return fillsList;
    }

    private String extractOrderTye(HttpResponse<JsonNode> jsonNodeHttpResponse) {
        return jsonNodeHttpResponse.getBody().getObject().getString("orderType");
    }

}
