package com.samatkinson.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.samatkinson.error.StockfighterException;
import com.samatkinson.model.Trade;
import org.json.JSONObject;

import java.io.IOException;

public class StockfighterResponse {
    private HttpResponse<JsonNode> jsonNodeHttpResponse;
    private ObjectMapper objectMapper = new ObjectMapper();

    public StockfighterResponse(HttpResponse<JsonNode> jsonNodeHttpResponse) {
        this.jsonNodeHttpResponse = jsonNodeHttpResponse;
    }

    public boolean getBoolean(String field) {
        return objectFromResponse().getBoolean(field);
    }

    private JSONObject objectFromResponse() {
        return jsonNodeHttpResponse.getBody().getObject();
    }


    public Trade as(Class<Trade> tradeClass) {
        try {
            return objectMapper.readValue(objectFromResponse().toString(), tradeClass);
        } catch (IOException e) {
            throw new StockfighterException("Error parsing JSON to class", e);
        }
    }

    public String getString(String error) {
        return objectFromResponse().getString(error);
    }
}
