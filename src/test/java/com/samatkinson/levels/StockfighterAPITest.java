package com.samatkinson.levels;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.samatkinson.api.StockfighterAPI;
import com.samatkinson.model.OrderBook;
import com.samatkinson.model.Trade;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StockfighterAPITest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);
    private String testServer = "http://localhost:8089";

    @Test
    public void wah() {
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

        Trade trade = new StockfighterAPI(testServer, "MFSEX", "BYSE").trade(104, price, account);

        assertThat(trade.fills.size(), is(1));
        assertThat(trade.fills.get(0).qty, is(filledTradeCount));
        assertThat(trade.orderType, is(orderType));

    }

    @Test
    public void retrieveOrderBook() throws Exception {
        String url = "/ob/api/venues/TESTEX/stocks/FOOBAR";
        stubFor(get(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "  \"ok\": true," +
                                "  \"venue\": \"TESTEX\"," +
                                "  \"symbol\": \"FOOBAR\"," +
                                "  \"bids\": " +
                                "    [" +
                                "      {\"price\": 5200, \"qty\": 1, \"isBuy\": true}," +
                                "      {\"price\": 815, \"qty\": 15, \"isBuy\": true}," +
                                "      {\"price\": 800, \"qty\": 12, \"isBuy\": true}," +
                                "      {\"price\": 800, \"qty\": 152, \"isBuy\": true}       " +
                                "    ]," +
                                "  \"asks\":" +
                                "   [" +
                                "      {\"price\": 5205, \"qty\": 150, \"isBuy\": false}," +
                                "      {\"price\": 5205, \"qty\": 1, \"isBuy\": false}," +
                                "      {\"price\": 10000000, \"qty\": 99999, \"isBuy\": false}" +
                                "   ]," +
                                "  \"ts\": \"2015-12-04T09:02:16.680986205Z\"" +
                                "}")));

        OrderBook orderBook = new StockfighterAPI(testServer, "FOOBAR", "TESTEX").orderBook();

        assertThat(orderBook.bids.size(), is(4));
        assertThat(orderBook.asks.size(), is(3));
        assertThat(orderBook.bids.get(0).price, is(5200));
        assertThat(orderBook.asks.get(2).price, is(10000000));
        assertThat(orderBook.asks.get(2).isBuy, is(false));

    }
}
