package com.samatkinson.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.samatkinson.error.StockfighterException;
import com.samatkinson.model.Trade;
import org.json.JSONObject;

import java.io.IOException;

public class StockfighterResponse {
    private String authKey = "276a779958d461a4724220bf4281f4dd3c4f6d01";

    private final HttpRequestWithBody request;
    private HttpResponse<JsonNode> jsonNodeHttpResponse;
    private ObjectMapper objectMapper = new ObjectMapper();

    public StockfighterResponse(HttpRequestWithBody request) {
        this.request = request;
    }

    public void execute(){
        try {
            jsonNodeHttpResponse = request.header("X-Starfighter-Authorization", authKey).asJson();
        } catch (UnirestException e) {
            throw new StockfighterException("Error in rest request", e);
        }
    }

    public boolean getBoolean(String field) {
        return objectFromResponse().getBoolean(field);
    }

    private JSONObject objectFromResponse() {
        return jsonNodeHttpResponse.getBody().getObject();
    }


    public Trade as(Class<Trade> tradeClass) {
        try {
            if(getBoolean("ok")){
                return objectMapper.readValue(objectFromResponse().toString(), tradeClass);
            }else{
                throw new StockfighterException(getString("error"));
            }
        } catch (IOException e) {
            throw new StockfighterException("Error parsing JSON to class", e);
        }
    }

    public String getString(String error) {
        return objectFromResponse().getString(error);
    }
}
