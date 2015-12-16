package com.samatkinson.levels;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.samatkinson.model.Trade;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LevelTwoTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void wah(){
        JSONAssert.assertEquals(
                "{\"account\": \"WPP20023868\",\"price\": 12311,\"qty\": 104,\"direction\": \"buy\",\"orderType\": \"market\"}",
                "{\"orderType\":\"market\",\"price\":12311,\"qty\":104,\"account\":\"WPP20023868\",\"direction\":\"buy\"}",
                JSONCompareMode.NON_EXTENSIBLE);

    }

    @Test
    public void testName() throws Exception {
        int filledTradeCount = 104;
        int price = 12311;
        String account = "WPP20023868";

        String orderType = "market";
        String url = "/ob/api/venues/MFSEX/stocks/BYSE/orders";

        stubFor(post(urlPathMatching(url))
                .withRequestBody(equalToJson(
                        "{" +
                                "\"orderType\": \"" + orderType + "\"," +
                                "\"price\": " + price + "," +
                                "\"qty\": " + filledTradeCount + "," +
                                "\"account\": \"" + account + "\"," +
                                "\"direction\": \"buy\"" +
                                "}", JSONCompareMode.LENIENT))
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

        LevelTwo levelTwo = new LevelTwo("http://localhost:8089", "MFSEX", "BYSE", account);

        Trade trade = levelTwo.trade(104, price);

        assertThat(trade.fills.size(), is(1));
        assertThat(trade.fills.get(0).qty, is(filledTradeCount));
        assertThat(trade.orderType, is(orderType));


    }
}
