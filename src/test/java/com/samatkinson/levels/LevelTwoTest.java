package com.samatkinson.levels;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.samatkinson.model.Trade;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LevelTwoTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void testName() throws Exception {

        int filledTradeCount = 104;
        String orderType = "market";
        String url = "https://api.stockfighter.io/ob/api/venues/MFSEX/stocks/BYSE/orders";
        stubFor(post(urlEqualTo(url))
                .withRequestBody(equalToJson(
                        "{" +
                                "    \"account\": \"WPP20023868\",\n" +
                                "    \"price\": 12311,\n" +
                                "    \"qty\": " + filledTradeCount + ",\n" +
                                "    \"direction\": \"buy\",\n" +
                                "    \"orderType\": \"" + orderType + "\"\n" +
                                "}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"ok\": true,\n" +
                                "    \"symbol\": \"BYSE\",\n" +
                                "    \"venue\": \"MFSEX\",\n" +
                                "    \"direction\": \"buy\",\n" +
                                "    \"originalQty\": " + filledTradeCount + ",\n" +
                                "    \"qty\": 0,\n" +
                                "    \"price\": 0,\n" +
                                "    \"orderType\": \"" + orderType + "\",\n" +
                                "    \"id\": 1051,\n" +
                                "    \"account\": \"WPP20023868\",\n" +
                                "    \"ts\": \"2015-12-16T04:18:46.42516234Z\",\n" +
                                "    \"fills\": [\n" +
                                "        {\n" +
                                "            \"price\": 3466,\n" +
                                "            \"qty\": " + filledTradeCount + ",\n" +
                                "            \"ts\": \"2015-12-16T04:18:46.425164757Z\"\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"totalFilled\": " + filledTradeCount + ",\n" +
                                "    \"open\": false\n" +
                                "}")));

        LevelTwo levelTwo = new LevelTwo("MFSEX", "BYSE", "WPP20023868");

        Trade trade = levelTwo.trade(104);

        assertThat(trade.fills.size(), is(1));
        assertThat(trade.fills.get(0).qty, is(filledTradeCount));
        assertThat(trade.orderType, is(orderType));


    }
}
