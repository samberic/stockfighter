package com.samatkinson.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.samatkinson.error.StockfighterException;
import com.samatkinson.model.Symbol;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockfighterResponse {
    private String authKey = "276a779958d461a4724220bf4281f4dd3c4f6d01";

    private final HttpRequest request;
    private HttpResponse<JsonNode> jsonNodeHttpResponse;
    private ObjectMapper objectMapper = new ObjectMapper();

    public StockfighterResponse(HttpRequest request) {
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


    public <T> T as(Class<T> tradeClass) {
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

    public <T> List<T> asListOf(Class<T> aClass, String arrayName) {
        try {
            JSONArray jsonArray = jsonNodeHttpResponse.getBody().getObject().getJSONArray(arrayName);

            List<T> result = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                result.add(objectMapper.readValue(jsonArray.getJSONObject(i).toString(), aClass));
            }

            return result;

        } catch (IOException e) {
            throw new StockfighterException("Error parsing JSON to class", e);
        }
    }
}
