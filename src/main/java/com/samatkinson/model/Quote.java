package com.samatkinson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    public String symbol;
    public String venue;
    public int bid;
    public int ask;
    public int bidSize;
    public int askSize;
    public int bidDepth;
    public int askDepth;
    public int last;
    public int lastSize;
    public Date lastTrade;
    public Date quoteTime;

    public Quote(){}
}
