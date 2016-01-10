package com.samatkinson.levels;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.samatkinson.api.StockfighterAPI;
import com.samatkinson.error.StockfighterException;
import com.samatkinson.model.OrderBook;
import com.samatkinson.model.Quote;
import com.samatkinson.model.Symbol;
import com.samatkinson.model.Trade;
import org.junit.ClassRule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class StockfighterAPITest {

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8089);
    private String testServer = "http://localhost:8089";

    @Test
    public void basicTradeExecutes() throws Exception {
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

        Trade trade = new StockfighterAPI(testServer, "MFSEX").trade(104, price, account, "BYSE", Trade.LIMIT);

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

        OrderBook orderBook = new StockfighterAPI(testServer, "TESTEX").orderBook("FOOBAR");

        assertThat(orderBook.bids.size(), is(4));
        assertThat(orderBook.asks.size(), is(3));
        assertThat(orderBook.bids.get(0).price, is(5200));
        assertThat(orderBook.asks.get(2).price, is(10000000));
        assertThat(orderBook.asks.get(2).isBuy, is(false));

    }

    @Test
    public void stocksOnExchange() throws Exception {
        String url = "/ob/api/venues/TESTEX/stocks";
        stubFor(get(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "  \"ok\": true," +
                                "  \"symbols\": [" +
                                "    {" +
                                "      \"name\": \"Foreign Owned Occulmancy\", " +
                                "     \"symbol\": \"FOO\"" +
                                "    }," +
                                "    {" +
                                "      \"name\": \"Best American Ricecookers\"," +
                                "      \"symbol\": \"BAR\"" +
                                "    }," +
                                "    {" +
                                "      \"name\": \"Badly Aliased Zebras\", " +
                                "      \"symbol\": \"BAZ\"" +
                                "    }" +
                                "  ] " +
                                "}")));

        List<Symbol> symbols = new StockfighterAPI(testServer, "TESTEX").symbols();

        assertThat(symbols.size(), is(3));

    }

    @Test
    public void stockQuote() throws Exception {
        String symbol = "FOOBAR";
        String url = "/ob/api/venues/TESTEX/stocks/" + symbol + "/quote";
        stubFor(get(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "    \"ok\": true," +
                                "    \"symbol\": \"" + symbol + "\"," +
                                "    \"venue\": \"TESTEX\"," +
                                "    \"bid\": 5100, " +
                                "    \"ask\": 5125, " +
                                "    \"bidSize\": 392, " +
                                "    \"askSize\": 711, " +
                                "    \"bidDepth\": 2748, " +
                                "    \"askDepth\": 2237, " +
                                "    \"last\": 5125, " +
                                "    \"lastSize\": 52," +
                                "    \"lastTrade\": \"2015-07-13T05:38:17.33640392Z\"," +
                                "    \"quoteTime\": \"2015-07-13T05:38:17.33640392Z\"" +
                                "}")));

        Quote quote = new StockfighterAPI(testServer, "TESTEX").stockQuote(symbol);

        assertThat(quote.bid, is(5100));
        assertThat(quote.ask, is(5125));

    }

    @Test
    public void orderStatus() throws Exception {
        String url = "/ob/api/venues/TESTEX/stocks/FOOBAR/orders/1234";
        stubFor(get(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "  \"ok\": true," +
                                "  \"symbol\": \"ROBO\"," +
                                "  \"venue\": \"ROBUST\"," +
                                "  \"direction\": \"buy\"," +
                                "  \"originalQty\": 85," +
                                "  \"qty\": 40," +
                                "  \"price\": 993," +
                                "  \"orderType\": \"immediate-or-cancel\"," +
                                "  \"id\": 1234," +
                                "  \"account\": \"FOO123\"," +
                                "  \"ts\": \"2015-08-10T16:10:32.987288+09:00\"," +
                                "  \"fills\": [" +
                                "    {" +
                                "      \"price\": 366," +
                                "      \"qty\": 45," +
                                "      \"ts\": \"2015-08-10T16:10:32.987292+09:00\"" +
                                "    }" +
                                "  ]," +
                                "  \"totalFilled\": 85," +
                                "  \"open\": true" +
                                "}")));

        Trade trade = new StockfighterAPI(testServer, "TESTEX").orderStatus(1234, "FOOBAR");

        assertThat(trade.fills.size(), is(1));
        assertThat(trade.totalFilled, is(85));
    }

    @Test
    public void cancel() throws Exception {
        String url = "/ob/api/venues/TESTEX/stocks/FOOBAR/orders/1234";
        stubFor(delete(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "  \"ok\": true," +
                                "  \"symbol\": \"ROBO\"," +
                                "  \"venue\": \"ROBUST\"," +
                                "  \"direction\": \"buy\"," +
                                "  \"originalQty\": 85," +
                                "  \"qty\": 0," +
                                "  \"price\": 993," +
                                "  \"orderType\": \"immediate-or-cancel\"," +
                                "  \"id\": 1," +
                                "  \"account\": \"FOO123\"," +
                                "  \"ts\": \"2015-08-10T16:10:32.987288+09:00\"," +
                                "  \"fills\": [" +
                                "    {" +
                                "      \"price\": 366," +
                                "      \"qty\": 45," +
                                "      \"ts\": \"2015-08-10T16:10:32.987292+09:00\"" +
                                "    }" +
                                "  ]," +
                                "  \"totalFilled\": 85," +
                                "  \"open\": false" +
                                "}")));

        Trade trade = new StockfighterAPI(testServer, "TESTEX").cancelOrder(1234, "FOOBAR");

        assertThat(trade.fills.size(), is(1));
        assertThat(trade.totalFilled, is(85));
        assertThat(trade.qty, is(0));
    }

    @Test
    public void whenACallFailsWillThrowAnException() throws Exception {
        String url = "/ob/api/venues/TESTEX2/stocks/FOOBAR/orders/1234";
        String errorMessage = "Not authorized to delete that order.  You have to own account ABC123456";
        stubFor(delete(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{" +
                                "  \"ok\": false," +
                                "  \"error\": \"" + errorMessage + ".\"" +
                                "}")));

        try {
            new StockfighterAPI(testServer, "TESTEX2").cancelOrder(1234, "FOOBAR");
        } catch (StockfighterException e) {
            assertThat(e.getMessage(), containsString(errorMessage));
            return;
        }
        fail("Should have recieved exception");
    }


}
