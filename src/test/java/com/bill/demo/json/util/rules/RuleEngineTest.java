package com.bill.demo.json.util.rules;

import org.junit.Test;

public class RuleEngineTest {
    public static final String json =
            "{\"data\":" +
                "[" +
                    "{"+
                    "\"id\":\"121212\"," +
                    "\"name\":\"xxyy\"," +
                    "\"desc\":\"example1\"" +
                    "},"+

                    "{"+
                    "\"id\":\"212121\"," +
                    "\"name\":\"yyxx\"," +
                    "\"desc\":\"example2\"" +
                    "}" +
                "]"+
            "}";

    @Test
    public void testFilter() throws Exception {
        RuleEngine engine = new RuleEngine();
        String queryString = "$filter=$.data[?(@.name=='xxyy')]";
        engine.action(json, queryString);
        System.out.println(engine.getContext());
    }

    @Test
    public void testSelect() throws Exception {
        RuleEngine engine = new RuleEngine();
        String queryString = "$select=$.data[*]['name','desc']";
        engine.action(json, queryString);
        System.out.println(engine.getContext());
    }

    @Test
    public void testFilterAndSelect() throws Exception {
        RuleEngine engine = new RuleEngine();
        String queryString = "$filter=$.data[?(@.name=='xxyy')]&$select=$.data[*]['name','desc']";
        engine.action(json, queryString);
        System.out.println(engine.getContext());
    }


}
